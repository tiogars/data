package fr.tiogars.data.cave.vinnom.controllers;

import java.nio.charset.StandardCharsets;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.cave.vinnom.services.VinNomExportCsvService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "vin-nom", description = "Operations liees a la gestion des vins.")
public class VinNomExportCsvController {
    private final VinNomExportCsvService vinNomExportCsvService;
    public VinNomExportCsvController(VinNomExportCsvService vinNomExportCsvService) { this.vinNomExportCsvService = vinNomExportCsvService; }
    @GetMapping(value = "/vin-nom/export/csv", produces = "text/csv")
    @Operation(summary = "Exporter les vins en CSV", description = "Retourne la liste complete des vins au format CSV.")
    public ResponseEntity<String> exportVinNomsCsv() {
        MediaType csvContentType = new MediaType("text", "csv", StandardCharsets.UTF_8);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"vin-nom-export.csv\"").contentType(csvContentType).body(vinNomExportCsvService.exportVinNomsAsCsv());
    }
}
