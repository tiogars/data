package fr.tiogars.data.settings.useraccount.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.settings.useraccount.models.UserAccountSearchResponse;
import fr.tiogars.data.settings.useraccount.services.UserAccountSearchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "user-account", description = "Operations liees a la gestion des comptes utilisateurs.")
public class UserAccountSearchController {

    private final UserAccountSearchService userAccountSearchService;

    public UserAccountSearchController(UserAccountSearchService userAccountSearchService) {
        this.userAccountSearchService = userAccountSearchService;
    }

    @GetMapping("/user-account/search")
    @Operation(summary = "Rechercher des comptes utilisateurs", description = "Cette operation permet de recuperer une liste paginee de comptes utilisateurs.")
    public ResponseEntity<UserAccountSearchResponse> searchUserAccounts(
        @Parameter(description = "Index de page (commence a 0).", example = "0")
        @RequestParam(defaultValue = "0") int page,
        @Parameter(description = "Nombre d'elements par page.", example = "10")
        @RequestParam(defaultValue = "10") int size,
        @Parameter(description = "Texte libre de recherche (username et role).", example = "admin")
        @RequestParam(required = false) String q
    ) {
        if (page < 0) {
            throw new IllegalArgumentException("Le parametre page doit etre superieur ou egal a 0.");
        }

        if (size <= 0) {
            throw new IllegalArgumentException("Le parametre size doit etre superieur a 0.");
        }

        if (size > 100) {
            throw new IllegalArgumentException("Le parametre size ne peut pas depasser 100.");
        }

        return ResponseEntity.ok(userAccountSearchService.searchUserAccounts(page, size, q));
    }
}
