package fr.tiogars.data.settings.auth.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.settings.auth.forms.AuthLoginForm;
import fr.tiogars.data.settings.auth.models.AuthLoginResponse;
import fr.tiogars.data.settings.auth.services.AuthLoginService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "auth", description = "Operations liees a l'authentification des comptes utilisateurs.")
public class AuthLoginController {

    private final AuthLoginService authLoginService;

    public AuthLoginController(AuthLoginService authLoginService) {
        this.authLoginService = authLoginService;
    }

    @PostMapping("/auth/login")
    @Operation(summary = "Se connecter", description = "Cette operation verifie les identifiants d'un compte utilisateur enregistre en base et retourne un jeton JWT.")
    public ResponseEntity<AuthLoginResponse> login(@RequestBody AuthLoginForm form) {
        return ResponseEntity.ok(authLoginService.login(form));
    }
}
