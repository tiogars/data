package fr.tiogars.data.locations.continent.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.locations.continent.services.ContinentDeleteOneService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * Contrôleur pour supprimer un continent.
 */
@RestController
@Tag(name = "continent", description = "Operations liees a la gestion des continents.")
public class ContinentDeleteOneController {

    private final ContinentDeleteOneService continentDeleteOneService;

    public ContinentDeleteOneController(ContinentDeleteOneService continentDeleteOneService) {
        this.continentDeleteOneService = continentDeleteOneService;
    }

    @DeleteMapping("/continent/{id}")
    @Operation(summary = "Supprimer un continent", description = "Cette operation permet de supprimer un continent par son identifiant.")
    public ResponseEntity<Void> deleteContinent(@PathVariable String id) {
        continentDeleteOneService.deleteContinent(id);
        return ResponseEntity.noContent().build();
    }
}
