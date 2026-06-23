package fr.tiogars.data.cave.cepage.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.cave.cepage.services.CepageDeleteOneService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "cepage", description = "Operations liees a la gestion des cépages.")
public class CepageDeleteOneController {
    private final CepageDeleteOneService cepageDeleteOneService;
    public CepageDeleteOneController(CepageDeleteOneService cepageDeleteOneService) { this.cepageDeleteOneService = cepageDeleteOneService; }
    @DeleteMapping("/cepage/{id}")
    @Operation(summary = "Gerer cépages", description = "Point d'entree cepage.")
    public ResponseEntity<Void> deleteCepage(@PathVariable String id) { cepageDeleteOneService.deleteCepage(id); return ResponseEntity.noContent().build(); }
}
