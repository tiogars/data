package fr.tiogars.data.products.gtin.controllers;

import java.nio.charset.StandardCharsets;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.products.gtin.services.GtinExportCsvService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "gtin", description = "Operations liees a la gestion des codes GTIN.")
public class GtinExportCsvController {

    private final GtinExportCsvService gtinExportCsvService;

    public GtinExportCsvController(GtinExportCsvService gtinExportCsvService) {
        this.gtinExportCsvService = gtinExportCsvService;
    }

    @GetMapping(value = "/gtin/export/csv", produces = "text/csv")
    @Operation(summary = "Exporter les GTIN en CSV", description = "Retourne la liste complete des GTIN au format CSV.")
    public ResponseEntity<String> exportGtinsCsv() {
        MediaType csvContentType = new MediaType("text", "csv", StandardCharsets.UTF_8);
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"gtin-export.csv\"")
            .contentType(csvContentType)
            .body(gtinExportCsvService.exportGtinsAsCsv());
    }
}
