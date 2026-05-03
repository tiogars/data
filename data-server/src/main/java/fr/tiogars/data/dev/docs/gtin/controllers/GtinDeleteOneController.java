package fr.tiogars.data.dev.docs.gtin.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.dev.docs.gtin.services.GtinDeleteOneService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "gtin", description = "Operations liees a la gestion des codes GTIN.")
public class GtinDeleteOneController {

    private final GtinDeleteOneService gtinDeleteOneService;

    public GtinDeleteOneController(GtinDeleteOneService gtinDeleteOneService) {
        this.gtinDeleteOneService = gtinDeleteOneService;
    }

    @DeleteMapping("/gtin/{id}")
    @Operation(summary = "Supprimer un GTIN", description = "Cette operation permet de supprimer un GTIN par son identifiant.")
    public ResponseEntity<Void> deleteGtin(@PathVariable String id) {
        gtinDeleteOneService.deleteGtin(id);
        return ResponseEntity.noContent().build();
    }
}
