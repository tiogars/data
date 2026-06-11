package fr.tiogars.data.games.brick.controllers;

import java.nio.charset.StandardCharsets;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.games.brick.services.BrickExportCsvService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "brick", description = "Gestion de la collection de briques et des liens externes associes.")
public class BrickExportCsvController {

    private final BrickExportCsvService brickExportCsvService;

    public BrickExportCsvController(BrickExportCsvService brickExportCsvService) {
        this.brickExportCsvService = brickExportCsvService;
    }

    @GetMapping(value = "/brick/export/csv", produces = "text/csv")
    @Operation(summary = "Exporter les briques en CSV", description = "Exporte l'etat des briques et liens externes au format CSV.")
    public ResponseEntity<String> exportBricksCsv() {
        MediaType csvContentType = new MediaType("text", "csv", StandardCharsets.UTF_8);
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"brick-export.csv\"")
            .contentType(csvContentType)
            .body(brickExportCsvService.exportStateAsCsv());
    }
}
