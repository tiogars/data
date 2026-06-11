package fr.tiogars.data.dev.model.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.dev.model.models.ModelSearchResponse;
import fr.tiogars.data.dev.model.services.ModelSearchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "model", description = "Operations liees a la gestion des modeles de donnees.")
public class ModelSearchController {

    private final ModelSearchService modelSearchService;

    public ModelSearchController(ModelSearchService modelSearchService) {
        this.modelSearchService = modelSearchService;
    }

    @GetMapping("/model/search")
    @Operation(summary = "Rechercher des modeles", description = "Cette operation permet de recuperer une liste paginee de modeles, avec recherche textuelle.")
    public ResponseEntity<ModelSearchResponse> searchModels(
        @Parameter(description = "Index de page (commence a 0).", example = "0")
        @RequestParam(defaultValue = "0") int page,
        @Parameter(description = "Nombre d'elements par page.", example = "10")
        @RequestParam(defaultValue = "10") int size,
        @Parameter(description = "Texte libre de recherche (nom et description).", example = "entity")
        @RequestParam(required = false) String q
    ) {
        if (page < 0) {
            throw new IllegalArgumentException("Le parametre page doit etre superieur ou egal a 0.");
        }

        if (size <= 0) {
            throw new IllegalArgumentException("Le parametre size doit etre superieur a 0.");
        }

        if (size > 100) {
            throw new IllegalArgumentException("Le parametre size ne peut pas depasser 100.");
        }

        return ResponseEntity.ok(modelSearchService.searchModels(page, size, q));
    }
}
