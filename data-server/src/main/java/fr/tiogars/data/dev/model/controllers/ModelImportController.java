package fr.tiogars.data.dev.model.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.dev.model.forms.ModelImportForm;
import fr.tiogars.data.dev.model.models.ModelImportResult;
import fr.tiogars.data.dev.model.services.ModelImportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "model", description = "Operations liees a la gestion des modeles de donnees.")
public class ModelImportController {

    private final ModelImportService modelImportService;

    public ModelImportController(ModelImportService modelImportService) {
        this.modelImportService = modelImportService;
    }

    @PostMapping("/model/import")
    @Operation(summary = "Importer les modeles", description = "Importe un JSON de modeles et remplace les donnees existantes.")
    public ResponseEntity<ModelImportResult> importModels(@RequestBody ModelImportForm form) {
        return ResponseEntity.ok(modelImportService.importModels(form != null ? form.getItems() : null));
    }
}
