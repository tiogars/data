package fr.tiogars.data.products.brand.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.products.brand.models.BrandImportResult;
import fr.tiogars.data.products.brand.services.BrandImportCsvService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "brand", description = "Operations liees a la gestion des marques.")
public class BrandImportCsvController {

    private final BrandImportCsvService brandImportCsvService;

    public BrandImportCsvController(BrandImportCsvService brandImportCsvService) {
        this.brandImportCsvService = brandImportCsvService;
    }

    @PostMapping(value = "/brand/import/csv", consumes = { "text/csv", "text/plain" })
    @Operation(summary = "Importer les marques en CSV", description = "Importe des marques au format CSV et applique les regles d'import existantes.")
    public ResponseEntity<BrandImportResult> importBrandsCsv(@RequestBody(required = false) String csvContent) {
        return ResponseEntity.ok(brandImportCsvService.importBrandsFromCsv(csvContent));
    }
}
