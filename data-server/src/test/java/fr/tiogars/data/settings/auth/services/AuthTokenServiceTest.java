package fr.tiogars.data.settings.auth.services;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Duration;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jwt.SignedJWT;

class AuthTokenServiceTest {

    @Test
    void shouldIssueTokenVerifiableWithPublishedPublicKey() throws Exception {
        AuthTokenService service = new AuthTokenService("data-server", Duration.ofMinutes(30));

        SignedJWT jwt = SignedJWT.parse(service.issueToken("tiogars", "ADMIN").value());

        JWKSet jwkSet = JWKSet.parse(service.publicJwkSet());
        RSAKey publicKey = (RSAKey) jwkSet.getKeyByKeyId(jwt.getHeader().getKeyID());
        assertThat(publicKey).isNotNull();
        assertThat(publicKey.isPrivate()).isFalse();
        assertThat(jwt.verify(new RSASSAVerifier(publicKey))).isTrue();
        assertThat(jwt.getJWTClaimsSet().getExpirationTime())
            .isAfter(jwt.getJWTClaimsSet().getIssueTime());
    }

    @Test
    void shouldNotExposePrivateKeyMaterialInJwkSet() {
        AuthTokenService service = new AuthTokenService("data-server", Duration.ofMinutes(30));

        @SuppressWarnings("unchecked")
        Map<String, Object> key = ((List<Map<String, Object>>) service.publicJwkSet().get("keys")).get(0);

        assertThat(key).containsKeys("kty", "n", "e", "kid").doesNotContainKeys("d", "p", "q", "dp", "dq", "qi");
    }
}
