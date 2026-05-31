package fr.tiogars.data.dev.model.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.dev.model.models.Model;
import fr.tiogars.data.dev.model.services.ModelGetOneService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "model", description = "Operations liees a la gestion des modeles de donnees.")
public class ModelGetOneController {

    private final ModelGetOneService modelGetOneService;

    public ModelGetOneController(ModelGetOneService modelGetOneService) {
        this.modelGetOneService = modelGetOneService;
    }

    @GetMapping("/model/{id}")
    @Operation(summary = "Recuperer un modele", description = "Cette operation permet de recuperer un modele par son identifiant.")
    public ResponseEntity<Model> getModel(@PathVariable String id) {
        return ResponseEntity.ok(modelGetOneService.getModel(id));
    }
}
