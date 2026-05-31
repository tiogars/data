package fr.tiogars.data.locations.continent.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.locations.continent.forms.ContinentUpdateForm;
import fr.tiogars.data.locations.continent.models.Continent;
import fr.tiogars.data.locations.continent.services.ContinentUpdateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * Contrôleur pour mettre à jour un continent.
 */
@RestController
@Tag(name = "continent", description = "Operations liees a la gestion des continents.")
public class ContinentUpdateController {

    private final ContinentUpdateService continentUpdateService;

    public ContinentUpdateController(ContinentUpdateService continentUpdateService) {
        this.continentUpdateService = continentUpdateService;
    }

    @PutMapping("/continent/{id}")
    @Operation(summary = "Mettre a jour un continent", description = "Cette operation permet de modifier un continent existant.")
    public ResponseEntity<Continent> updateContinent(@PathVariable String id, @RequestBody ContinentUpdateForm form) {
        return ResponseEntity.ok(continentUpdateService.updateContinent(id, form));
    }
}
