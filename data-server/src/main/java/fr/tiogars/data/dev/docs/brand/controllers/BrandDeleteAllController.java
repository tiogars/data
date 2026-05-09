package fr.tiogars.data.dev.docs.brand.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.dev.docs.brand.services.BrandDeleteAllService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "brand", description = "Operations liees a la gestion des marques.")
public class BrandDeleteAllController {

    private final BrandDeleteAllService brandDeleteAllService;

    public BrandDeleteAllController(BrandDeleteAllService brandDeleteAllService) {
        this.brandDeleteAllService = brandDeleteAllService;
    }

    @DeleteMapping("/brand")
    @Operation(summary = "Supprimer toutes les marques", description = "Cette operation permet de Supprimer toutes les marques.")
    public ResponseEntity<Void> deleteAllBrands() {
        brandDeleteAllService.deleteAllBrands();
        return ResponseEntity.noContent().build();
    }
}
