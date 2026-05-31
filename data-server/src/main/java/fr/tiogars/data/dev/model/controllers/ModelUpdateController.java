package fr.tiogars.data.dev.model.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.dev.model.models.Model;
import fr.tiogars.data.dev.model.services.ModelUpdateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "model", description = "Operations liees a la gestion des modeles de donnees.")
public class ModelUpdateController {

    private final ModelUpdateService modelUpdateService;

    public ModelUpdateController(ModelUpdateService modelUpdateService) {
        this.modelUpdateService = modelUpdateService;
    }

    @PutMapping("/model/{id}")
    @Operation(summary = "Mettre a jour un modele", description = "Cette operation permet de modifier un modele existant.")
    public ResponseEntity<Model> updateModel(@PathVariable String id, @RequestBody Model model) {
        return ResponseEntity.ok(modelUpdateService.updateModel(id, model));
    }
}
