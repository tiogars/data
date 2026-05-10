package fr.tiogars.data.dev.docs.model.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.dev.docs.model.services.ModelDeleteOneService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "model", description = "Operations liees a la gestion des modeles de donnees.")
public class ModelDeleteOneController {

    private final ModelDeleteOneService modelDeleteOneService;

    public ModelDeleteOneController(ModelDeleteOneService modelDeleteOneService) {
        this.modelDeleteOneService = modelDeleteOneService;
    }

    @DeleteMapping("/model/{id}")
    @Operation(summary = "Supprimer un modele", description = "Cette operation permet de supprimer un modele par son identifiant.")
    public ResponseEntity<Void> deleteModel(@PathVariable String id) {
        modelDeleteOneService.deleteModel(id);
        return ResponseEntity.noContent().build();
    }
}
