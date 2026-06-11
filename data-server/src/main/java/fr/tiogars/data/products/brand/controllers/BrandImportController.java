package fr.tiogars.data.products.brand.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.products.brand.forms.BrandImportForm;
import fr.tiogars.data.products.brand.models.BrandImportResult;
import fr.tiogars.data.products.brand.services.BrandImportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "brand", description = "Operations liees a la gestion des marques.")
public class BrandImportController {

    private final BrandImportService brandImportService;

    public BrandImportController(BrandImportService brandImportService) {
        this.brandImportService = brandImportService;
    }

    @PostMapping("/brand/import")
    @Operation(summary = "Importer les marques", description = "Importe des marques depuis un texte (une ligne par marque) ou via le format JSON historique.")
    public ResponseEntity<BrandImportResult> importBrands(@RequestBody BrandImportForm form) {
        return ResponseEntity.ok(brandImportService.importBrands(form));
    }
}
