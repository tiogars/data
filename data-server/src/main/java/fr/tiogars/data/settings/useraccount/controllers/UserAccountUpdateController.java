package fr.tiogars.data.settings.useraccount.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.settings.useraccount.forms.UserAccountUpdateForm;
import fr.tiogars.data.settings.useraccount.models.UserAccount;
import fr.tiogars.data.settings.useraccount.services.UserAccountUpdateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "user-account", description = "Operations liees a la gestion des comptes utilisateurs.")
public class UserAccountUpdateController {

    private final UserAccountUpdateService userAccountUpdateService;

    public UserAccountUpdateController(UserAccountUpdateService userAccountUpdateService) {
        this.userAccountUpdateService = userAccountUpdateService;
    }

    @PutMapping("/user-account/{id}")
    @Operation(summary = "Mettre a jour un compte utilisateur", description = "Cette operation permet de modifier un compte utilisateur existant.")
    public ResponseEntity<UserAccount> updateUserAccount(@PathVariable String id, @RequestBody UserAccountUpdateForm form) {
        return ResponseEntity.ok(userAccountUpdateService.updateUserAccount(id, form));
    }
}
