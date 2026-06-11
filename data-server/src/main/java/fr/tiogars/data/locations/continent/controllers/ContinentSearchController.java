package fr.tiogars.data.locations.continent.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.locations.continent.models.ContinentSearchResponse;
import fr.tiogars.data.locations.continent.services.ContinentSearchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "continent", description = "Operations liees a la gestion des continents.")
public class ContinentSearchController {

    private final ContinentSearchService continentSearchService;

    public ContinentSearchController(ContinentSearchService continentSearchService) {
        this.continentSearchService = continentSearchService;
    }

    @GetMapping("/continent/search")
    @Operation(summary = "Rechercher des continents", description = "Cette operation permet de recuperer une liste paginee de continents, avec recherche textuelle.")
    public ResponseEntity<ContinentSearchResponse> searchContinents(
        @Parameter(description = "Index de page (commence a 0).", example = "0")
        @RequestParam(defaultValue = "0") int page,
        @Parameter(description = "Nombre d'elements par page.", example = "10")
        @RequestParam(defaultValue = "10") int size,
        @Parameter(description = "Texte libre de recherche (code et nom).", example = "eu")
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

        return ResponseEntity.ok(continentSearchService.searchContinents(page, size, q));
    }
}
