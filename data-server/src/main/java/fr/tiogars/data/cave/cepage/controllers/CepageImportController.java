package fr.tiogars.data.cave.cepage.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.cave.cepage.forms.CepageImportForm;
import fr.tiogars.data.cave.cepage.models.CepageImportResult;
import fr.tiogars.data.cave.cepage.services.CepageImportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "cepage", description = "Operations liees a la gestion des cépages.")
public class CepageImportController {
    private final CepageImportService cepageImportService;
    public CepageImportController(CepageImportService cepageImportService) { this.cepageImportService = cepageImportService; }
    @PostMapping("/cepage/import")
    @Operation(summary = "Gerer cépages", description = "Point d'entree cepage.")
    public ResponseEntity<CepageImportResult> importCepages(@RequestBody CepageImportForm form) { return ResponseEntity.ok(cepageImportService.importCepages(form)); }
}
