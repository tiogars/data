package fr.tiogars.data.games.brick.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.games.brick.models.BrickSearchResponse;
import fr.tiogars.data.games.brick.services.BrickSearchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "brick", description = "Gestion de la collection de briques et des liens externes associes.")
public class BrickSearchController {

    private final BrickSearchService brickSearchService;

    public BrickSearchController(BrickSearchService brickSearchService) {
        this.brickSearchService = brickSearchService;
    }

    @GetMapping("/brick/search")
    @Operation(summary = "Rechercher des briques", description = "Cette operation permet de recuperer une liste paginee de briques, avec recherche textuelle.")
    public ResponseEntity<BrickSearchResponse> searchBricks(
        @Parameter(description = "Index de page (commence a 0).", example = "0")
        @RequestParam(defaultValue = "0") int page,
        @Parameter(description = "Nombre d'elements par page.", example = "10")
        @RequestParam(defaultValue = "10") int size,
        @Parameter(description = "Texte libre de recherche (numero, titre, tags).", example = "technic")
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

        return ResponseEntity.ok(brickSearchService.searchBricks(page, size, q));
    }
}
