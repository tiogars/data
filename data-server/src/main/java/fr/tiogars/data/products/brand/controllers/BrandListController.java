package fr.tiogars.data.products.brand.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.products.brand.models.BrandListResponse;
import fr.tiogars.data.products.brand.services.BrandListService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "brand", description = "Operations liees a la gestion des marques.")
public class BrandListController {

    private final BrandListService brandListService;

    public BrandListController(BrandListService brandListService) {
        this.brandListService = brandListService;
    }

    @GetMapping("/brand/list")
    @Operation(summary = "Lister les marques", description = "Cette operation permet de recuperer la liste des marques.")
    public ResponseEntity<BrandListResponse> listBrands() {
        return ResponseEntity.ok(brandListService.listBrands());
    }
}
