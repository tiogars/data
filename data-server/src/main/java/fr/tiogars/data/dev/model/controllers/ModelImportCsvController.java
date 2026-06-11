package fr.tiogars.data.dev.model.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.dev.model.models.ModelImportResult;
import fr.tiogars.data.dev.model.services.ModelImportCsvService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "model", description = "Operations liees a la gestion des modeles de donnees.")
public class ModelImportCsvController {

    private final ModelImportCsvService modelImportCsvService;

    public ModelImportCsvController(ModelImportCsvService modelImportCsvService) {
        this.modelImportCsvService = modelImportCsvService;
    }

    @PostMapping(value = "/model/import/csv", consumes = { "text/csv", "text/plain" })
    @Operation(summary = "Importer les modeles en CSV", description = "Importe des modeles au format CSV et remplace les donnees existantes.")
    public ResponseEntity<ModelImportResult> importModelsCsv(@RequestBody(required = false) String csvContent) {
        return ResponseEntity.ok(modelImportCsvService.importModelsFromCsv(csvContent));
    }
}
