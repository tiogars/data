package fr.tiogars.data.cave.circonstance.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.cave.circonstance.models.CirconstanceImportResult;
import fr.tiogars.data.cave.circonstance.services.CirconstanceImportCsvService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "circonstance", description = "Operations liees a la gestion des circonstances.")
public class CirconstanceImportCsvController {
    private final CirconstanceImportCsvService circonstanceImportCsvService;
    public CirconstanceImportCsvController(CirconstanceImportCsvService circonstanceImportCsvService) { this.circonstanceImportCsvService = circonstanceImportCsvService; }
    @PostMapping(value = "/circonstance/import/csv", consumes = { "text/csv", "text/plain" })
    @Operation(summary = "Gerer circonstances", description = "Point d'entree circonstance.")
    public ResponseEntity<CirconstanceImportResult> importCirconstancesCsv(@RequestBody(required = false) String csvContent) { return ResponseEntity.ok(circonstanceImportCsvService.importCirconstancesFromCsv(csvContent)); }
}
