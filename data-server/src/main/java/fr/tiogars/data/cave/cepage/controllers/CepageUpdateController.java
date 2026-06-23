package fr.tiogars.data.cave.cepage.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.cave.cepage.models.Cepage;
import fr.tiogars.data.cave.cepage.services.CepageUpdateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "cepage", description = "Operations liees a la gestion des cépages.")
public class CepageUpdateController {
    private final CepageUpdateService cepageUpdateService;
    public CepageUpdateController(CepageUpdateService cepageUpdateService) { this.cepageUpdateService = cepageUpdateService; }
    @PutMapping("/cepage/{id}")
    @Operation(summary = "Gerer cépages", description = "Point d'entree cepage.")
    public ResponseEntity<Cepage> updateCepage(@PathVariable String id, @RequestBody Cepage cepage) { return ResponseEntity.ok(cepageUpdateService.updateCepage(id, cepage)); }
}
