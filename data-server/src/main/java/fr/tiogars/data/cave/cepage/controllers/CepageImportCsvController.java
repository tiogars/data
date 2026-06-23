package fr.tiogars.data.cave.cepage.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.cave.cepage.models.CepageImportResult;
import fr.tiogars.data.cave.cepage.services.CepageImportCsvService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "cepage", description = "Operations liees a la gestion des cépages.")
public class CepageImportCsvController {
    private final CepageImportCsvService cepageImportCsvService;
    public CepageImportCsvController(CepageImportCsvService cepageImportCsvService) { this.cepageImportCsvService = cepageImportCsvService; }
    @PostMapping(value = "/cepage/import/csv", consumes = { "text/csv", "text/plain" })
    @Operation(summary = "Gerer cépages", description = "Point d'entree cepage.")
    public ResponseEntity<CepageImportResult> importCepagesCsv(@RequestBody(required = false) String csvContent) { return ResponseEntity.ok(cepageImportCsvService.importCepagesFromCsv(csvContent)); }
}
