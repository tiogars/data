package fr.tiogars.data.dev.model.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.dev.model.models.ModelPrintResponse;
import fr.tiogars.data.dev.model.services.ModelPrintService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "model", description = "Operations liees a la gestion des modeles de donnees.")
public class ModelPrintController {

    private final ModelPrintService modelPrintService;

    public ModelPrintController(ModelPrintService modelPrintService) {
        this.modelPrintService = modelPrintService;
    }

    @GetMapping("/model/print")
    @Operation(summary = "Imprimer les modeles", description = "Retourne les donnees d'impression en mode filtered ou all avec metadonnees.")
    public ResponseEntity<ModelPrintResponse> printModels(
        @RequestParam(required = false) String mode,
        @RequestParam(required = false) String name,
        @RequestParam(required = false) String description
    ) {
        return ResponseEntity.ok(modelPrintService.printModels(mode, name, description));
    }
}
