package fr.tiogars.data.dev.docs.brand.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.dev.docs.brand.forms.BrandCreationForm;
import fr.tiogars.data.dev.docs.brand.models.Brand;
import fr.tiogars.data.dev.docs.brand.services.BrandCreationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "brand", description = "Operations liees a la gestion des marques.")
public class BrandCreationController {

    private final BrandCreationService brandCreationService;

    public BrandCreationController(BrandCreationService brandCreationService) {
        this.brandCreationService = brandCreationService;
    }

    @PostMapping("/brand")
    @Operation(summary = "Creer une marque", description = "Cette operation permet de Creer une marque.")
    public ResponseEntity<Brand> createBrand(@RequestBody BrandCreationForm form) {
        return ResponseEntity.ok(brandCreationService.createBrand(form));
    }
}
