package fr.tiogars.data.products.brand.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.products.brand.models.Brand;
import fr.tiogars.data.products.brand.services.BrandGetOneService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "brand", description = "Operations liees a la gestion des marques.")
public class BrandGetOneController {

    private final BrandGetOneService brandGetOneService;

    public BrandGetOneController(BrandGetOneService brandGetOneService) {
        this.brandGetOneService = brandGetOneService;
    }

    @GetMapping("/brand/{id}")
    @Operation(summary = "Recuperer une marque", description = "Cette operation permet de Recuperer une marque par son identifiant.")
    public ResponseEntity<Brand> getBrand(@PathVariable String id) {
        return ResponseEntity.ok(brandGetOneService.getBrand(id));
    }
}
