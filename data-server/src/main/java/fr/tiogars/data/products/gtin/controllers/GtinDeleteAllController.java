package fr.tiogars.data.products.gtin.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.products.gtin.services.GtinDeleteAllService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "gtin", description = "Operations liees a la gestion des codes GTIN.")
public class GtinDeleteAllController {

    private final GtinDeleteAllService gtinDeleteAllService;

    public GtinDeleteAllController(GtinDeleteAllService gtinDeleteAllService) {
        this.gtinDeleteAllService = gtinDeleteAllService;
    }

    @DeleteMapping("/gtin")
    @Operation(summary = "Supprimer tous les GTIN", description = "Cette operation permet de supprimer tous les GTIN.")
    public ResponseEntity<Void> deleteAllGtins() {
        gtinDeleteAllService.deleteAllGtins();
        return ResponseEntity.noContent().build();
    }
}
