package fr.tiogars.data.products.brand.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.products.brand.services.BrandDeleteOneService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "brand", description = "Operations liees a la gestion des marques.")
public class BrandDeleteOneController {

    private final BrandDeleteOneService brandDeleteOneService;

    public BrandDeleteOneController(BrandDeleteOneService brandDeleteOneService) {
        this.brandDeleteOneService = brandDeleteOneService;
    }

    @DeleteMapping("/brand/{id}")
    @Operation(summary = "Supprimer une marque", description = "Cette operation permet de Supprimer une marque par son identifiant.")
    public ResponseEntity<Void> deleteBrand(@PathVariable String id) {
        brandDeleteOneService.deleteBrand(id);
        return ResponseEntity.noContent().build();
    }
}
