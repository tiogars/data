package fr.tiogars.data.cave.appellation.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.cave.appellation.forms.AppellationImportForm;
import fr.tiogars.data.cave.appellation.models.AppellationImportResult;
import fr.tiogars.data.cave.appellation.services.AppellationImportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "appellation", description = "Operations liees a la gestion des appellations.")
public class AppellationImportController {
    private final AppellationImportService appellationImportService;
    public AppellationImportController(AppellationImportService appellationImportService) { this.appellationImportService = appellationImportService; }
    @PostMapping("/appellation/import")
    @Operation(summary = "Gerer appellations", description = "Point d'entree appellation.")
    public ResponseEntity<AppellationImportResult> importAppellations(@RequestBody AppellationImportForm form) { return ResponseEntity.ok(appellationImportService.importAppellations(form)); }
}
