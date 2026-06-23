package fr.tiogars.data.cave.circonstance.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.cave.circonstance.models.CirconstanceListResponse;
import fr.tiogars.data.cave.circonstance.services.CirconstanceExportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "circonstance", description = "Operations liees a la gestion des circonstances.")
public class CirconstanceExportController {
    private final CirconstanceExportService circonstanceExportService;
    public CirconstanceExportController(CirconstanceExportService circonstanceExportService) { this.circonstanceExportService = circonstanceExportService; }
    @GetMapping("/circonstance/export")
    @Operation(summary = "Gerer circonstances", description = "Point d'entree circonstance.")
    public ResponseEntity<CirconstanceListResponse> exportCirconstances() { return ResponseEntity.ok(circonstanceExportService.exportCirconstances()); }
}
