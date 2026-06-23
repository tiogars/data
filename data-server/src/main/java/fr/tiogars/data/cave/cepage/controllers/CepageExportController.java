package fr.tiogars.data.cave.cepage.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.cave.cepage.models.CepageListResponse;
import fr.tiogars.data.cave.cepage.services.CepageExportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "cepage", description = "Operations liees a la gestion des cépages.")
public class CepageExportController {
    private final CepageExportService cepageExportService;
    public CepageExportController(CepageExportService cepageExportService) { this.cepageExportService = cepageExportService; }
    @GetMapping("/cepage/export")
    @Operation(summary = "Gerer cépages", description = "Point d'entree cepage.")
    public ResponseEntity<CepageListResponse> exportCepages() { return ResponseEntity.ok(cepageExportService.exportCepages()); }
}
