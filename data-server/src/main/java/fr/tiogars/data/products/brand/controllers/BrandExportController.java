package fr.tiogars.data.products.brand.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.products.brand.models.BrandListResponse;
import fr.tiogars.data.products.brand.services.BrandExportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "brand", description = "Operations liees a la gestion des marques.")
public class BrandExportController {

    private final BrandExportService brandExportService;

    public BrandExportController(BrandExportService brandExportService) {
        this.brandExportService = brandExportService;
    }

    @GetMapping("/brand/export")
    @Operation(summary = "Exporter les marques", description = "Retourne la liste complete des marques en JSON.")
    public ResponseEntity<BrandListResponse> exportBrands() {
        return ResponseEntity.ok(brandExportService.exportBrands());
    }
}
