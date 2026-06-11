package fr.tiogars.data.products.brand.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.products.brand.models.BrandPrintResponse;
import fr.tiogars.data.products.brand.services.BrandPrintService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "brand", description = "Operations liees a la gestion des marques.")
public class BrandPrintController {

    private final BrandPrintService brandPrintService;

    public BrandPrintController(BrandPrintService brandPrintService) {
        this.brandPrintService = brandPrintService;
    }

    @GetMapping("/brand/print")
    @Operation(summary = "Imprimer les marques", description = "Retourne les donnees d'impression en mode filtered ou all avec metadonnees.")
    public ResponseEntity<BrandPrintResponse> printBrands(
        @RequestParam(required = false) String mode,
        @RequestParam(required = false) String name,
        @RequestParam(required = false) String description
    ) {
        return ResponseEntity.ok(brandPrintService.printBrands(mode, name, description));
    }
}
