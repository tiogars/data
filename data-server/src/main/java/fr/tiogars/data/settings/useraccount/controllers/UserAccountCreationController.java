package fr.tiogars.data.settings.useraccount.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.settings.useraccount.forms.UserAccountCreationForm;
import fr.tiogars.data.settings.useraccount.models.UserAccount;
import fr.tiogars.data.settings.useraccount.services.UserAccountCreationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "user-account", description = "Operations liees a la gestion des comptes utilisateurs.")
public class UserAccountCreationController {

    private final UserAccountCreationService userAccountCreationService;

    public UserAccountCreationController(UserAccountCreationService userAccountCreationService) {
        this.userAccountCreationService = userAccountCreationService;
    }

    @PostMapping("/user-account")
    @Operation(summary = "Creer un compte utilisateur", description = "Cette operation permet de creer un compte utilisateur interne.")
    public ResponseEntity<UserAccount> createUserAccount(@RequestBody UserAccountCreationForm form) {
        return ResponseEntity.ok(userAccountCreationService.createUserAccount(form));
    }
}
