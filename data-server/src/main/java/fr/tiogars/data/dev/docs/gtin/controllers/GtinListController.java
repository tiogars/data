package fr.tiogars.data.dev.docs.gtin.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.dev.docs.gtin.models.GtinListResponse;
import fr.tiogars.data.dev.docs.gtin.services.GtinListService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "gtin", description = "Operations liees a la gestion des codes GTIN.")
public class GtinListController {

    private final GtinListService gtinListService;

    public GtinListController(GtinListService gtinListService) {
        this.gtinListService = gtinListService;
    }

    @GetMapping("/gtin")
    @Operation(summary = "Lister les GTIN", description = "Cette operation permet de recuperer la liste des GTIN.")
    public ResponseEntity<GtinListResponse> listGtins() {
        return ResponseEntity.ok(gtinListService.listGtins());
    }
}
