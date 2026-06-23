package fr.tiogars.data.cave.circonstance.controllers;

import java.nio.charset.StandardCharsets;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.cave.circonstance.services.CirconstanceExportCsvService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "circonstance", description = "Operations liees a la gestion des circonstances.")
public class CirconstanceExportCsvController {
    private final CirconstanceExportCsvService circonstanceExportCsvService;
    public CirconstanceExportCsvController(CirconstanceExportCsvService circonstanceExportCsvService) { this.circonstanceExportCsvService = circonstanceExportCsvService; }
    @GetMapping(value = "/circonstance/export/csv", produces = "text/csv")
    @Operation(summary = "Exporter circonstances en CSV", description = "Retourne la liste complete au format CSV.")
    public ResponseEntity<String> exportCirconstancesCsv() {
        MediaType csvContentType = new MediaType("text", "csv", StandardCharsets.UTF_8);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"circonstance-export.csv\"").contentType(csvContentType).body(circonstanceExportCsvService.exportCirconstancesAsCsv());
    }
}
