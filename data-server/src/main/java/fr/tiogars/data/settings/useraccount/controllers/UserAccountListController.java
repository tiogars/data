package fr.tiogars.data.settings.useraccount.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.settings.useraccount.models.UserAccountListResponse;
import fr.tiogars.data.settings.useraccount.services.UserAccountListService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "user-account", description = "Operations liees a la gestion des comptes utilisateurs.")
public class UserAccountListController {

    private final UserAccountListService userAccountListService;

    public UserAccountListController(UserAccountListService userAccountListService) {
        this.userAccountListService = userAccountListService;
    }

    @GetMapping("/user-account/list")
    @Operation(summary = "Lister les comptes utilisateurs", description = "Cette operation permet de recuperer la liste des comptes utilisateurs.")
    public ResponseEntity<UserAccountListResponse> listUserAccounts() {
        return ResponseEntity.ok(userAccountListService.listUserAccounts());
    }
}
