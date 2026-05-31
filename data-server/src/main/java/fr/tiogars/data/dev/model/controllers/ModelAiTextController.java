package fr.tiogars.data.dev.model.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.dev.model.models.ModelAiTextResponse;
import fr.tiogars.data.dev.model.services.ModelAiTextService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "model", description = "Operations liees a la gestion des modeles de donnees.")
public class ModelAiTextController {

    private final ModelAiTextService modelAiTextService;

    public ModelAiTextController(ModelAiTextService modelAiTextService) {
        this.modelAiTextService = modelAiTextService;
    }

    @GetMapping("/model/{id}/ai-text")
    @Operation(summary = "Convertir un modele en texte IA", description = "Retourne un texte pret a donner a une IA pour generer un nouveau modele.")
    public ResponseEntity<ModelAiTextResponse> getModelAiText(@PathVariable String id) {
        return ResponseEntity.ok(modelAiTextService.buildAiText(id));
    }
}
