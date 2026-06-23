package fr.tiogars.data.cave.circonstance.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.cave.circonstance.forms.CirconstanceImportForm;
import fr.tiogars.data.cave.circonstance.models.CirconstanceImportResult;
import fr.tiogars.data.cave.circonstance.services.CirconstanceImportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "circonstance", description = "Operations liees a la gestion des circonstances.")
public class CirconstanceImportController {
    private final CirconstanceImportService circonstanceImportService;
    public CirconstanceImportController(CirconstanceImportService circonstanceImportService) { this.circonstanceImportService = circonstanceImportService; }
    @PostMapping("/circonstance/import")
    @Operation(summary = "Gerer circonstances", description = "Point d'entree circonstance.")
    public ResponseEntity<CirconstanceImportResult> importCirconstances(@RequestBody CirconstanceImportForm form) { return ResponseEntity.ok(circonstanceImportService.importCirconstances(form)); }
}
