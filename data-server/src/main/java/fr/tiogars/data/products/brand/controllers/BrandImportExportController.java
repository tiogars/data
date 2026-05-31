package fr.tiogars.data.products.brand.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.products.brand.forms.BrandImportForm;
import fr.tiogars.data.products.brand.models.BrandImportResult;
import fr.tiogars.data.products.brand.models.BrandListResponse;
import fr.tiogars.data.products.brand.services.BrandImportExportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "brand", description = "Operations liees a la gestion des marques.")
public class BrandImportExportController {

    private final BrandImportExportService brandImportExportService;

    public BrandImportExportController(BrandImportExportService brandImportExportService) {
        this.brandImportExportService = brandImportExportService;
    }

    @GetMapping("/brand/export")
    @Operation(summary = "Exporter les marques", description = "Retourne la liste complete des marques en JSON.")
    public ResponseEntity<BrandListResponse> exportBrands() {
        return ResponseEntity.ok(brandImportExportService.exportBrands());
    }

    @PostMapping("/brand/import")
    @Operation(summary = "Importer les marques", description = "Importe un JSON de marques et remplace les donnees existantes.")
    public ResponseEntity<BrandImportResult> importBrands(@RequestBody BrandImportForm form) {
        return ResponseEntity.ok(brandImportExportService.importBrands(form != null ? form.getItems() : null));
    }
}