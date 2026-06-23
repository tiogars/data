package fr.tiogars.data.cave.cepage.controllers;

import java.nio.charset.StandardCharsets;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.cave.cepage.services.CepageExportCsvService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "cepage", description = "Operations liees a la gestion des cépages.")
public class CepageExportCsvController {
    private final CepageExportCsvService cepageExportCsvService;
    public CepageExportCsvController(CepageExportCsvService cepageExportCsvService) { this.cepageExportCsvService = cepageExportCsvService; }
    @GetMapping(value = "/cepage/export/csv", produces = "text/csv")
    @Operation(summary = "Exporter cépages en CSV", description = "Retourne la liste complete au format CSV.")
    public ResponseEntity<String> exportCepagesCsv() {
        MediaType csvContentType = new MediaType("text", "csv", StandardCharsets.UTF_8);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"cepage-export.csv\"").contentType(csvContentType).body(cepageExportCsvService.exportCepagesAsCsv());
    }
}
