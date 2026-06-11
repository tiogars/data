package fr.tiogars.data.dev.model.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.dev.model.models.ModelListResponse;
import fr.tiogars.data.dev.model.services.ModelExportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "model", description = "Operations liees a la gestion des modeles de donnees.")
public class ModelExportController {

    private final ModelExportService modelExportService;

    public ModelExportController(ModelExportService modelExportService) {
        this.modelExportService = modelExportService;
    }

    @GetMapping("/model/export")
    @Operation(summary = "Exporter les modeles", description = "Retourne la liste complete des modeles en JSON.")
    public ResponseEntity<ModelListResponse> exportModels() {
        return ResponseEntity.ok(modelExportService.exportModels());
    }
}
