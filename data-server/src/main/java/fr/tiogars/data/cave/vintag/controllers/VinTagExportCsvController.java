package fr.tiogars.data.cave.vintag.controllers;

import java.nio.charset.StandardCharsets;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.cave.vintag.services.VinTagExportCsvService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "vin-tag", description = "Operations liees a la gestion des tags de vin.")
public class VinTagExportCsvController {
    private final VinTagExportCsvService vinTagExportCsvService;
    public VinTagExportCsvController(VinTagExportCsvService vinTagExportCsvService) { this.vinTagExportCsvService = vinTagExportCsvService; }
    @GetMapping(value = "/vin-tag/export/csv", produces = "text/csv")
    @Operation(summary = "Exporter tags de vin en CSV", description = "Retourne la liste complete au format CSV.")
    public ResponseEntity<String> exportVinTagsCsv() {
        MediaType csvContentType = new MediaType("text", "csv", StandardCharsets.UTF_8);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"vin-tag-export.csv\"").contentType(csvContentType).body(vinTagExportCsvService.exportVinTagsAsCsv());
    }
}
