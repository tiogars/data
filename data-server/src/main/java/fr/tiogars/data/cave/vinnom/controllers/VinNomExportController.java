package fr.tiogars.data.cave.vinnom.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.cave.vinnom.models.VinNomListResponse;
import fr.tiogars.data.cave.vinnom.services.VinNomExportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "vin-nom", description = "Operations liees a la gestion des vins.")
public class VinNomExportController {
    private final VinNomExportService vinNomExportService;
    public VinNomExportController(VinNomExportService vinNomExportService) { this.vinNomExportService = vinNomExportService; }
    @GetMapping("/vin-nom/export")
    @Operation(summary = "Exporter les vins", description = "Retourne la liste complete des vins en JSON.")
    public ResponseEntity<VinNomListResponse> exportVinNoms() { return ResponseEntity.ok(vinNomExportService.exportVinNoms()); }
}
