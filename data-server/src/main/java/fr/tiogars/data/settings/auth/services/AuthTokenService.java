package fr.tiogars.data.settings.auth.services;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JOSEObjectType;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.KeyUse;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.gen.RSAKeyGenerator;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

/**
 * Emet les jetons JWT des comptes utilisateurs enregistres en base et publie la cle publique
 * correspondante, que la gateway utilise pour verifier les jetons.
 *
 * <p>La paire de cles RSA est generee au demarrage et reste en memoire : aucun secret n'est a
 * partager ni a configurer, mais les sessions existantes sont invalidees a chaque redemarrage.</p>
 */
@Service
public class AuthTokenService {

    private final RSAKey signingKey;
    private final String issuer;
    private final Duration validity;

    public AuthTokenService(
        @Value("${data.auth.jwt.issuer:data-server}") String issuer,
        @Value("${data.auth.jwt.validity:PT8H}") Duration validity
    ) {
        this.issuer = issuer;
        this.validity = validity;
        try {
            this.signingKey = new RSAKeyGenerator(2048)
                .keyID(UUID.randomUUID().toString())
                .keyUse(KeyUse.SIGNATURE)
                .algorithm(JWSAlgorithm.RS256)
                .generate();
        } catch (JOSEException exception) {
            throw new IllegalStateException("Impossible de generer la cle de signature des jetons.", exception);
        }
    }

    /**
     * Emet un jeton signe pour l'utilisateur donne.
     *
     * @param username nom de connexion, utilise comme sujet du jeton
     * @param role role du compte (ADMIN ou USER)
     * @return le jeton serialise et sa duree de validite
     */
    public IssuedToken issueToken(String username, String role) {
        Instant issuedAt = Instant.now();
        Instant expiresAt = issuedAt.plus(validity);

        JWTClaimsSet claims = new JWTClaimsSet.Builder()
            .issuer(issuer)
            .subject(username)
            .claim("preferred_username", username)
            .claim("roles", List.of(role))
            .issueTime(Date.from(issuedAt))
            .expirationTime(Date.from(expiresAt))
            .jwtID(UUID.randomUUID().toString())
            .build();

        JWSHeader header = new JWSHeader.Builder(JWSAlgorithm.RS256)
            .keyID(signingKey.getKeyID())
            .type(JOSEObjectType.JWT)
            .build();

        try {
            SignedJWT jwt = new SignedJWT(header, claims);
            jwt.sign(new RSASSASigner(signingKey));
            return new IssuedToken(jwt.serialize(), validity);
        } catch (JOSEException exception) {
            throw new IllegalStateException("Impossible de signer le jeton d'authentification.", exception);
        }
    }

    /**
     * Retourne la cle publique de verification au format JWKS (RFC 7517).
     */
    public Map<String, Object> publicJwkSet() {
        return new JWKSet(signingKey.toPublicJWK()).toJSONObject();
    }

    /**
     * Jeton emis et duree pendant laquelle il reste valide.
     */
    public record IssuedToken(String value, Duration validity) {
    }
}
