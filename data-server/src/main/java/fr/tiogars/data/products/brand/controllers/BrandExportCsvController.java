package fr.tiogars.data.products.brand.controllers;

import java.nio.charset.StandardCharsets;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.products.brand.services.BrandExportCsvService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "brand", description = "Operations liees a la gestion des marques.")
public class BrandExportCsvController {

    private final BrandExportCsvService brandExportCsvService;

    public BrandExportCsvController(BrandExportCsvService brandExportCsvService) {
        this.brandExportCsvService = brandExportCsvService;
    }

    @GetMapping(value = "/brand/export/csv", produces = "text/csv")
    @Operation(summary = "Exporter les marques en CSV", description = "Retourne la liste complete des marques au format CSV.")
    public ResponseEntity<String> exportBrandsCsv() {
        MediaType csvContentType = new MediaType("text", "csv", StandardCharsets.UTF_8);
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"brand-export.csv\"")
            .contentType(csvContentType)
            .body(brandExportCsvService.exportBrandsAsCsv());
    }
}
