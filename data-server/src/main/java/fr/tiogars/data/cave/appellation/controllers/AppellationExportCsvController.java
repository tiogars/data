package fr.tiogars.data.cave.appellation.controllers;

import java.nio.charset.StandardCharsets;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.cave.appellation.services.AppellationExportCsvService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "appellation", description = "Operations liees a la gestion des appellations.")
public class AppellationExportCsvController {
    private final AppellationExportCsvService appellationExportCsvService;
    public AppellationExportCsvController(AppellationExportCsvService appellationExportCsvService) { this.appellationExportCsvService = appellationExportCsvService; }
    @GetMapping(value = "/appellation/export/csv", produces = "text/csv")
    @Operation(summary = "Exporter appellations en CSV", description = "Retourne la liste complete au format CSV.")
    public ResponseEntity<String> exportAppellationsCsv() {
        MediaType csvContentType = new MediaType("text", "csv", StandardCharsets.UTF_8);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"appellation-export.csv\"").contentType(csvContentType).body(appellationExportCsvService.exportAppellationsAsCsv());
    }
}
