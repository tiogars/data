package fr.tiogars.data.products.brand.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.products.brand.models.BrandSearchResponse;
import fr.tiogars.data.products.brand.services.BrandSearchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "brand", description = "Operations liees a la gestion des marques.")
public class BrandSearchController {

    private final BrandSearchService brandSearchService;

    public BrandSearchController(BrandSearchService brandSearchService) {
        this.brandSearchService = brandSearchService;
    }

    @GetMapping("/brand/search")
    @Operation(summary = "Rechercher des marques", description = "Cette operation permet de recuperer une liste paginee de marques, avec recherche textuelle.")
    public ResponseEntity<BrandSearchResponse> searchBrands(
        @Parameter(description = "Index de page (commence a 0).", example = "0")
        @RequestParam(defaultValue = "0") int page,
        @Parameter(description = "Nombre d'elements par page.", example = "10")
        @RequestParam(defaultValue = "10") int size,
        @Parameter(description = "Texte libre de recherche (nom et description).", example = "lego")
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

        return ResponseEntity.ok(brandSearchService.searchBrands(page, size, q));
    }
}
