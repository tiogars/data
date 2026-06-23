package fr.tiogars.data.cave.cepage.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.cave.cepage.services.CepageDeleteAllService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "cepage", description = "Operations liees a la gestion des cépages.")
public class CepageDeleteAllController {
    private final CepageDeleteAllService cepageDeleteAllService;
    public CepageDeleteAllController(CepageDeleteAllService cepageDeleteAllService) { this.cepageDeleteAllService = cepageDeleteAllService; }
    @DeleteMapping("/cepage")
    @Operation(summary = "Gerer cépages", description = "Point d'entree cepage.")
    public ResponseEntity<Void> deleteAllCepages() { cepageDeleteAllService.deleteAllCepages(); return ResponseEntity.noContent().build(); }
}
