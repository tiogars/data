package fr.tiogars.data.dev.model.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.dev.model.models.ModelListResponse;
import fr.tiogars.data.dev.model.services.ModelListService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "model", description = "Operations liees a la gestion des modeles de donnees.")
public class ModelListController {

    private final ModelListService modelListService;

    public ModelListController(ModelListService modelListService) {
        this.modelListService = modelListService;
    }

    @GetMapping("/model")
    @Operation(summary = "Lister les modeles", description = "Cette operation permet de recuperer la liste des modeles de donnees.")
    public ResponseEntity<ModelListResponse> listModels() {
        return ResponseEntity.ok(modelListService.listModels());
    }
}
