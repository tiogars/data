package fr.tiogars.data.cave.vin.controllers;

import java.nio.charset.StandardCharsets;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.cave.vin.services.VinExportCsvService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "vin", description = "Operations liees a la gestion des vins degustes.")
public class VinExportCsvController {

    private final VinExportCsvService vinExportCsvService;

    public VinExportCsvController(VinExportCsvService vinExportCsvService) {
        this.vinExportCsvService = vinExportCsvService;
    }

    @GetMapping(value = "/vin/export/csv", produces = "text/csv")
    @Operation(summary = "Exporter les vins en CSV", description = "Retourne la liste complete des vins au format CSV.")
    public ResponseEntity<String> exportVinsCsv() {
        MediaType csvContentType = new MediaType("text", "csv", StandardCharsets.UTF_8);
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"vin-export.csv\"")
            .contentType(csvContentType)
            .body(vinExportCsvService.exportVinsAsCsv());
    }
}
