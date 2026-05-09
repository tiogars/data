package fr.tiogars.data.dev.docs.brand.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.dev.docs.brand.models.Brand;
import fr.tiogars.data.dev.docs.brand.services.BrandUpdateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "brand", description = "Operations liees a la gestion des marques.")
public class BrandUpdateController {

    private final BrandUpdateService brandUpdateService;

    public BrandUpdateController(BrandUpdateService brandUpdateService) {
        this.brandUpdateService = brandUpdateService;
    }

    @PutMapping("/brand/{id}")
    @Operation(summary = "Mettre a jour une marque", description = "Cette operation permet de modifier une marque existante.")
    public ResponseEntity<Brand> updateBrand(@PathVariable String id, @RequestBody Brand brand) {
        return ResponseEntity.ok(brandUpdateService.updateBrand(id, brand));
    }
}
