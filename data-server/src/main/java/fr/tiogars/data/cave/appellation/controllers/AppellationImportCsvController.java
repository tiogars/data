package fr.tiogars.data.cave.appellation.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.cave.appellation.models.AppellationImportResult;
import fr.tiogars.data.cave.appellation.services.AppellationImportCsvService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "appellation", description = "Operations liees a la gestion des appellations.")
public class AppellationImportCsvController {
    private final AppellationImportCsvService appellationImportCsvService;
    public AppellationImportCsvController(AppellationImportCsvService appellationImportCsvService) { this.appellationImportCsvService = appellationImportCsvService; }
    @PostMapping(value = "/appellation/import/csv", consumes = { "text/csv", "text/plain" })
    @Operation(summary = "Gerer appellations", description = "Point d'entree appellation.")
    public ResponseEntity<AppellationImportResult> importAppellationsCsv(@RequestBody(required = false) String csvContent) { return ResponseEntity.ok(appellationImportCsvService.importAppellationsFromCsv(csvContent)); }
}
