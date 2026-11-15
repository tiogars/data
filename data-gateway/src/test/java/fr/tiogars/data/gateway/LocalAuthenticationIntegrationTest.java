package fr.tiogars.data.gateway;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureTestRestTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.gen.RSAKeyGenerator;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import okhttp3.mockwebserver.Dispatcher;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestRestTemplate
class LocalAuthenticationIntegrationTest {

	private static final String KEY_ID = "test-key";
	private static final String ISSUER = "data-server";

	private static MockWebServer dataServer;
	private static RSAKey signingKey;

	@Autowired
	private TestRestTemplate testRestTemplate;

	@BeforeAll
	static void beforeAll() throws IOException, JOSEException {
		signingKey = new RSAKeyGenerator(2048).keyID(KEY_ID).generate();
		String jwks = new JWKSet(signingKey.toPublicJWK()).toString();

		dataServer = new MockWebServer();
		dataServer.setDispatcher(new Dispatcher() {
			@Override
			public MockResponse dispatch(RecordedRequest request) {
				if ("/auth/jwks".equals(request.getPath())) {
					return new MockResponse().setResponseCode(200)
							.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
							.setBody(jwks);
				}
				return new MockResponse().setResponseCode(200).setBody("downstream-ok");
			}
		});
		dataServer.start();
	}

	@AfterAll
	static void afterAll() throws IOException {
		dataServer.shutdown();
	}

	@DynamicPropertySource
	static void overrideProperties(DynamicPropertyRegistry registry) {
		registry.add("data.gateway.downstream-base-url", () -> dataServer.url("/").toString());
		registry.add("data.gateway.domain-discovery.enabled", () -> false);
		registry.add("data.gateway.security.enabled", () -> true);
		registry.add("data.gateway.rate-limit.login-capacity", () -> 3);
		registry.add("data.gateway.rate-limit.login-period", () -> "PT10M");
	}

	@Test
	void shouldProxyRequestWhenLocalTokenIsValid() throws Exception {
		String token = token(signingKey, ISSUER, List.of("ADMIN"), 3600);

		ResponseEntity<String> response = get("/server-info/java-version", token, "203.0.113.10");

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody()).isEqualTo("downstream-ok");
	}

	@Test
	void shouldReturnUnauthorizedWhenTokenIsMissing() {
		ResponseEntity<String> response = get("/server-info/java-version", null, "203.0.113.11");

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
	}

	@Test
	void shouldReturnUnauthorizedWhenTokenIsSignedWithAnotherKey() throws Exception {
		RSAKey otherKey = new RSAKeyGenerator(2048).keyID(KEY_ID).generate();

		ResponseEntity<String> response = get(
				"/server-info/java-version", token(otherKey, ISSUER, List.of("ADMIN"), 3600), "203.0.113.12");

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
	}

	@Test
	void shouldReturnUnauthorizedWhenTokenIsExpired() throws Exception {
		ResponseEntity<String> response = get(
				"/server-info/java-version", token(signingKey, ISSUER, List.of("ADMIN"), -3600), "203.0.113.13");

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
	}

	@Test
	void shouldReturnUnauthorizedWhenIssuerIsNotTrusted() throws Exception {
		ResponseEntity<String> response = get(
				"/server-info/java-version", token(signingKey, "https://evil.example/", List.of("ADMIN"), 3600), "203.0.113.14");

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
	}

	@Test
	void shouldReturnForbiddenWhenRoleIsNotAllowed() throws Exception {
		ResponseEntity<String> response = get(
				"/server-info/java-version", token(signingKey, ISSUER, List.of("gateway-admin"), 3600), "203.0.113.15");

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
	}

	@Test
	void shouldNotExposeJwkSetThroughGateway() throws Exception {
		String token = token(signingKey, ISSUER, List.of("ADMIN"), 3600);

		ResponseEntity<String> response = get("/auth/jwks", token, "203.0.113.16");

		assertThat(response.getStatusCode().is4xxClientError()).isTrue();
	}

	@Test
	void shouldForwardLoginRequestWithoutToken() {
		ResponseEntity<String> response = post("/auth/login", "203.0.113.17");

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody()).isEqualTo("downstream-ok");
	}

	@Test
	void shouldReturnTooManyRequestsWhenLoginAttemptsExceedQuota() {
		String clientIp = "203.0.113.18";

		for (int attempt = 0; attempt < 3; attempt++) {
			assertThat(post("/auth/login", clientIp).getStatusCode()).isEqualTo(HttpStatus.OK);
		}

		assertThat(post("/auth/login", clientIp).getStatusCode()).isEqualTo(HttpStatus.TOO_MANY_REQUESTS);
	}

	private ResponseEntity<String> get(String path, String token, String clientIp) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("X-Forwarded-For", clientIp);
		if (token != null) {
			headers.setBearerAuth(token);
		}
		return testRestTemplate.exchange(path, HttpMethod.GET, new HttpEntity<>(headers), String.class);
	}

	private ResponseEntity<String> post(String path, String clientIp) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("X-Forwarded-For", clientIp);
		headers.setContentType(MediaType.APPLICATION_JSON);
		return testRestTemplate.exchange(
				path, HttpMethod.POST, new HttpEntity<>("{\"username\":\"u\",\"password\":\"p\"}", headers), String.class);
	}

	private static String token(RSAKey key, String issuer, List<String> roles, long validitySeconds) throws JOSEException {
		long now = System.currentTimeMillis();
		JWTClaimsSet claims = new JWTClaimsSet.Builder()
				.issuer(issuer)
				.subject("tiogars")
				.claim("roles", roles)
				.issueTime(new Date(now - 7_200_000))
				.expirationTime(new Date(now + validitySeconds * 1000))
				.jwtID(UUID.randomUUID().toString())
				.build();
		SignedJWT jwt = new SignedJWT(new JWSHeader.Builder(JWSAlgorithm.RS256).keyID(key.getKeyID()).build(), claims);
		jwt.sign(new RSASSASigner(key));
		return jwt.serialize();
	}
}
