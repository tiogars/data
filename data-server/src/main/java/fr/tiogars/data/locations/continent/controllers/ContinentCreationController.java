package fr.tiogars.data.locations.continent.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.locations.continent.forms.ContinentCreationForm;
import fr.tiogars.data.locations.continent.models.Continent;
import fr.tiogars.data.locations.continent.services.ContinentCreationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * Contrôleur pour créer un continent.
 */
@RestController
@Tag(name = "continent", description = "Operations liees a la gestion des continents.")
public class ContinentCreationController {

    private final ContinentCreationService continentCreationService;

    public ContinentCreationController(ContinentCreationService continentCreationService) {
        this.continentCreationService = continentCreationService;
    }

    @PostMapping("/continent")
    @Operation(summary = "Creer un continent", description = "Cette operation permet de creer un continent.")
    public ResponseEntity<Continent> createContinent(@RequestBody ContinentCreationForm form) {
        return ResponseEntity.ok(continentCreationService.createContinent(form));
    }
}
