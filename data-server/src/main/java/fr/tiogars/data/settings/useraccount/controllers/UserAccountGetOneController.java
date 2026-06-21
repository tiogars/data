package fr.tiogars.data.settings.useraccount.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.settings.useraccount.models.UserAccount;
import fr.tiogars.data.settings.useraccount.services.UserAccountGetOneService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "user-account", description = "Operations liees a la gestion des comptes utilisateurs.")
public class UserAccountGetOneController {

    private final UserAccountGetOneService userAccountGetOneService;

    public UserAccountGetOneController(UserAccountGetOneService userAccountGetOneService) {
        this.userAccountGetOneService = userAccountGetOneService;
    }

    @GetMapping("/user-account/{id}")
    @Operation(summary = "Recuperer un compte utilisateur", description = "Cette operation permet de recuperer un compte utilisateur par son identifiant.")
    public ResponseEntity<UserAccount> getUserAccount(@PathVariable String id) {
        return ResponseEntity.ok(userAccountGetOneService.getUserAccount(id));
    }
}
