package fr.tiogars.data.dev.model.controllers;

import java.nio.charset.StandardCharsets;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.dev.model.services.ModelExportCsvService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "model", description = "Operations liees a la gestion des modeles de donnees.")
public class ModelExportCsvController {

    private final ModelExportCsvService modelExportCsvService;

    public ModelExportCsvController(ModelExportCsvService modelExportCsvService) {
        this.modelExportCsvService = modelExportCsvService;
    }

    @GetMapping(value = "/model/export/csv", produces = "text/csv")
    @Operation(summary = "Exporter les modeles en CSV", description = "Retourne la liste complete des modeles au format CSV.")
    public ResponseEntity<String> exportModelsCsv() {
        MediaType csvContentType = new MediaType("text", "csv", StandardCharsets.UTF_8);
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"model-export.csv\"")
            .contentType(csvContentType)
            .body(modelExportCsvService.exportModelsAsCsv());
    }
}
