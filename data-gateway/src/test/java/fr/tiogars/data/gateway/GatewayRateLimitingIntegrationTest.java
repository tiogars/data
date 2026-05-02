package fr.tiogars.data.gateway;

import java.io.IOException;
import java.util.List;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
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
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestRestTemplate
class GatewayRateLimitingIntegrationTest {

	private static MockWebServer mockWebServer;

	@Autowired
	private TestRestTemplate testRestTemplate;

	@BeforeAll
	static void beforeAll() throws IOException {
		mockWebServer = new MockWebServer();
		mockWebServer.start();
	}

	@AfterAll
	static void afterAll() throws IOException {
		mockWebServer.shutdown();
	}

	@DynamicPropertySource
	static void overrideProperties(DynamicPropertyRegistry registry) {
		registry.add("data.gateway.downstream-base-url", () -> mockWebServer.url("/").toString());
		registry.add("data.gateway.rate-limit.capacity", () -> 2);
		registry.add("data.gateway.rate-limit.period", () -> "PT10M");
		registry.add("data.gateway.rate-limit.tokens", () -> 1);
	}

	@Test
	void shouldReturnTooManyRequestsWhenQuotaIsExceeded() throws InterruptedException {
		mockWebServer.enqueue(new MockResponse().setResponseCode(200).setBody("ok-1"));
		mockWebServer.enqueue(new MockResponse().setResponseCode(200).setBody("ok-2"));

		ResponseEntity<String> first = testRestTemplate.getForEntity("/api/ratelimit-check", String.class);
		ResponseEntity<String> second = testRestTemplate.getForEntity("/api/ratelimit-check", String.class);
		ResponseEntity<String> third = testRestTemplate.getForEntity("/api/ratelimit-check", String.class);

		assertThat(first.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(second.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(third.getStatusCode()).isEqualTo(HttpStatus.TOO_MANY_REQUESTS);
		assertThat(mockWebServer.takeRequest().getPath()).isEqualTo("/api/ratelimit-check");
		assertThat(mockWebServer.takeRequest().getPath()).isEqualTo("/api/ratelimit-check");
	}

	@Test
	void shouldRouteBusinessPathsToDownstream() throws InterruptedException {
		List<String> paths = List.of(
				"/server-info/java-version",
				"/section",
				"/footer-link",
				"/github-repository",
				"/github-rest-config/abc"
		);

		for (int i = 0; i < paths.size(); i++) {
			String path = paths.get(i);
			String expectedBody = "ok-" + i;
			mockWebServer.enqueue(new MockResponse().setResponseCode(200).setBody(expectedBody));

			HttpHeaders headers = new HttpHeaders();
			headers.add("X-Forwarded-For", "203.0.113." + (40 + i));
			HttpEntity<Void> request = new HttpEntity<>(headers);

			ResponseEntity<String> response = testRestTemplate.exchange(
					path,
					HttpMethod.GET,
					request,
					String.class
			);

			assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
			assertThat(response.getBody()).isEqualTo(expectedBody);
			assertThat(mockWebServer.takeRequest().getPath()).isEqualTo(path);
		}
	}
}
