package fr.tiogars.data.dev.model.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.dev.model.services.ModelDeleteAllService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "model", description = "Operations liees a la gestion des modeles de donnees.")
public class ModelDeleteAllController {

    private final ModelDeleteAllService modelDeleteAllService;

    public ModelDeleteAllController(ModelDeleteAllService modelDeleteAllService) {
        this.modelDeleteAllService = modelDeleteAllService;
    }

    @DeleteMapping("/model")
    @Operation(summary = "Supprimer tous les modeles", description = "Cette operation permet de supprimer tous les modeles de donnees.")
    public ResponseEntity<Void> deleteAllModels() {
        modelDeleteAllService.deleteAllModels();
        return ResponseEntity.noContent().build();
    }
}
