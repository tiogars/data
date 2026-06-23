package fr.tiogars.data.cave.vin.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.cave.vin.models.VinListResponse;
import fr.tiogars.data.cave.vin.services.VinExportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "vin", description = "Operations liees a la gestion des vins degustes.")
public class VinExportController {

    private final VinExportService vinExportService;

    public VinExportController(VinExportService vinExportService) {
        this.vinExportService = vinExportService;
    }

    @GetMapping("/vin/export")
    @Operation(summary = "Exporter les vins", description = "Retourne la liste complete des vins en JSON.")
    public ResponseEntity<VinListResponse> exportVins() {
        return ResponseEntity.ok(vinExportService.exportVins());
    }
}
