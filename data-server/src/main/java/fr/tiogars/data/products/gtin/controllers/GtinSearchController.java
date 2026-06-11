package fr.tiogars.data.products.gtin.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.products.gtin.models.GtinSearchResponse;
import fr.tiogars.data.products.gtin.services.GtinSearchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "gtin", description = "Operations liees a la gestion des codes GTIN.")
public class GtinSearchController {

    private final GtinSearchService gtinSearchService;

    public GtinSearchController(GtinSearchService gtinSearchService) {
        this.gtinSearchService = gtinSearchService;
    }

    @GetMapping("/gtin/search")
    @Operation(summary = "Rechercher des GTIN", description = "Cette operation permet de recuperer une liste paginee de GTIN, avec recherche textuelle.")
    public ResponseEntity<GtinSearchResponse> searchGtins(
        @Parameter(description = "Index de page (commence a 0).", example = "0")
        @RequestParam(defaultValue = "0") int page,
        @Parameter(description = "Nombre d'elements par page.", example = "10")
        @RequestParam(defaultValue = "10") int size,
        @Parameter(description = "Texte libre de recherche (code et description).", example = "123")
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

        return ResponseEntity.ok(gtinSearchService.searchGtins(page, size, q));
    }
}
