package fr.tiogars.data.products.gtin.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.products.gtin.models.GtinListResponse;
import fr.tiogars.data.products.gtin.services.GtinExportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "gtin", description = "Operations liees a la gestion des codes GTIN.")
public class GtinExportController {

    private final GtinExportService gtinExportService;

    public GtinExportController(GtinExportService gtinExportService) {
        this.gtinExportService = gtinExportService;
    }

    @GetMapping("/gtin/export")
    @Operation(summary = "Exporter les GTIN", description = "Retourne la liste complete des GTIN en JSON.")
    public ResponseEntity<GtinListResponse> exportGtins() {
        return ResponseEntity.ok(gtinExportService.exportGtins());
    }
}
