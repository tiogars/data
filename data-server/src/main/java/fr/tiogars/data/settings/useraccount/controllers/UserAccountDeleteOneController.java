package fr.tiogars.data.settings.useraccount.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.settings.useraccount.services.UserAccountDeleteOneService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "user-account", description = "Operations liees a la gestion des comptes utilisateurs.")
public class UserAccountDeleteOneController {

    private final UserAccountDeleteOneService userAccountDeleteOneService;

    public UserAccountDeleteOneController(UserAccountDeleteOneService userAccountDeleteOneService) {
        this.userAccountDeleteOneService = userAccountDeleteOneService;
    }

    @DeleteMapping("/user-account/{id}")
    @Operation(summary = "Supprimer un compte utilisateur", description = "Cette operation permet de supprimer un compte utilisateur par son identifiant.")
    public ResponseEntity<Void> deleteUserAccount(@PathVariable String id) {
        userAccountDeleteOneService.deleteUserAccount(id);
        return ResponseEntity.noContent().build();
    }
}
