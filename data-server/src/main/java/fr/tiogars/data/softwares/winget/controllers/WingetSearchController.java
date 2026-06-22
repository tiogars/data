package fr.tiogars.data.softwares.winget.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.softwares.winget.models.WingetSearchResponse;
import fr.tiogars.data.softwares.winget.services.WingetSearchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "winget", description = "Operations liees a la gestion des applications installables par Winget.")
public class WingetSearchController {

    private final WingetSearchService wingetSearchService;

    public WingetSearchController(WingetSearchService wingetSearchService) {
        this.wingetSearchService = wingetSearchService;
    }

    @GetMapping("/winget/search")
    @Operation(summary = "Rechercher des applications Winget", description = "Cette operation permet de recuperer une liste paginee d'applications Winget, avec recherche textuelle.")
    public ResponseEntity<WingetSearchResponse> searchWingets(
        @Parameter(description = "Index de page (commence a 0).", example = "0")
        @RequestParam(defaultValue = "0") int page,
        @Parameter(description = "Nombre d'elements par page.", example = "10")
        @RequestParam(defaultValue = "10") int size,
        @Parameter(description = "Texte libre de recherche (nom, winget id, commande, description).", example = "Notepad")
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

        return ResponseEntity.ok(wingetSearchService.searchWingets(page, size, q));
    }
}
