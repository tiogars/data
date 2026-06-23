package fr.tiogars.data.cave.couleur.controllers;

import java.nio.charset.StandardCharsets;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.cave.couleur.services.CouleurExportCsvService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "couleur", description = "Operations liees a la gestion des couleurs.")
public class CouleurExportCsvController {
    private final CouleurExportCsvService couleurExportCsvService;
    public CouleurExportCsvController(CouleurExportCsvService couleurExportCsvService) { this.couleurExportCsvService = couleurExportCsvService; }
    @GetMapping(value = "/couleur/export/csv", produces = "text/csv")
    @Operation(summary = "Exporter couleurs en CSV", description = "Retourne la liste complete au format CSV.")
    public ResponseEntity<String> exportCouleursCsv() {
        MediaType csvContentType = new MediaType("text", "csv", StandardCharsets.UTF_8);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"couleur-export.csv\"").contentType(csvContentType).body(couleurExportCsvService.exportCouleursAsCsv());
    }
}
