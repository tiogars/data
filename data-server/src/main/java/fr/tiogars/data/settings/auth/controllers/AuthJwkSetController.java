package fr.tiogars.data.settings.auth.controllers;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.settings.auth.services.AuthTokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "auth", description = "Operations liees a l'authentification des comptes utilisateurs.")
public class AuthJwkSetController {

    private final AuthTokenService authTokenService;

    public AuthJwkSetController(AuthTokenService authTokenService) {
        this.authTokenService = authTokenService;
    }

    @GetMapping("/auth/jwks")
    @Operation(summary = "Recuperer les cles publiques", description = "Cette operation retourne la cle publique (JWKS) utilisee pour verifier les jetons emis a la connexion.")
    public ResponseEntity<Map<String, Object>> getJwkSet() {
        return ResponseEntity.ok(authTokenService.publicJwkSet());
    }
}
