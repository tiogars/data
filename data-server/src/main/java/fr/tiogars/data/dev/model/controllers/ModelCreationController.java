package fr.tiogars.data.dev.model.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.dev.model.forms.ModelCreationForm;
import fr.tiogars.data.dev.model.models.Model;
import fr.tiogars.data.dev.model.services.ModelCreationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "model", description = "Operations liees a la gestion des modeles de donnees.")
public class ModelCreationController {

    private final ModelCreationService modelCreationService;

    public ModelCreationController(ModelCreationService modelCreationService) {
        this.modelCreationService = modelCreationService;
    }

    @PostMapping("/model")
    @Operation(summary = "Creer un modele", description = "Cette operation permet de creer un modele de donnees.")
    public ResponseEntity<Model> createModel(@RequestBody ModelCreationForm form) {
        return ResponseEntity.ok(modelCreationService.createModel(form));
    }
}
