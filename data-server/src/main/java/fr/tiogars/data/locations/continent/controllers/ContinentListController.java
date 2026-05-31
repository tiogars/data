package fr.tiogars.data.locations.continent.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.locations.continent.models.ContinentListResponse;
import fr.tiogars.data.locations.continent.services.ContinentListService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * Contrôleur pour récupérer la liste des continents.
 */
@RestController
@Tag(name = "continent", description = "Operations liees a la gestion des continents.")
public class ContinentListController {

    private final ContinentListService continentListService;

    public ContinentListController(ContinentListService continentListService) {
        this.continentListService = continentListService;
    }

    @GetMapping("/continent")
    @Operation(summary = "Lister les continents", description = "Cette operation permet de recuperer la liste des continents.")
    public ResponseEntity<ContinentListResponse> listContinents() {
        return ResponseEntity.ok(continentListService.listContinents());
    }
}
