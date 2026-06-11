package fr.tiogars.data.settings.urlmanager.controllers;

import java.nio.charset.StandardCharsets;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.settings.urlmanager.services.UrlManagerExportCsvService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "url-manager", description = "Gestion des URLs taguees et des cartes d'accueil.")
public class UrlManagerExportCsvController {

    private final UrlManagerExportCsvService urlManagerExportCsvService;

    public UrlManagerExportCsvController(UrlManagerExportCsvService urlManagerExportCsvService) {
        this.urlManagerExportCsvService = urlManagerExportCsvService;
    }

    @GetMapping(value = "/url-manager/export/csv", produces = "text/csv")
    @Operation(summary = "Exporter l'etat URL manager en CSV", description = "Retourne l'etat complet au format CSV.")
    public ResponseEntity<String> exportStateCsv() {
        MediaType csvContentType = new MediaType("text", "csv", StandardCharsets.UTF_8);
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"url-manager-export.csv\"")
            .contentType(csvContentType)
            .body(urlManagerExportCsvService.exportStateAsCsv());
    }
}
