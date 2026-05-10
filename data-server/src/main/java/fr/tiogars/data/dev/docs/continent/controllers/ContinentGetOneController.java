package fr.tiogars.data.dev.docs.continent.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.dev.docs.continent.models.Continent;
import fr.tiogars.data.dev.docs.continent.services.ContinentGetOneService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * Contrôleur pour récupérer un continent.
 */
@RestController
@Tag(name = "continent", description = "Operations liees a la gestion des continents.")
public class ContinentGetOneController {

    private final ContinentGetOneService continentGetOneService;

    public ContinentGetOneController(ContinentGetOneService continentGetOneService) {
        this.continentGetOneService = continentGetOneService;
    }

    @GetMapping("/continent/{id}")
    @Operation(summary = "Recuperer un continent", description = "Cette operation permet de recuperer un continent par son identifiant.")
    public ResponseEntity<Continent> getContinent(@PathVariable String id) {
        return ResponseEntity.ok(continentGetOneService.getContinent(id));
    }
}
