package fr.tiogars.data.products.gtin.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.products.gtin.models.Gtin;
import fr.tiogars.data.products.gtin.services.GtinUpdateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "gtin", description = "Operations liees a la gestion des codes GTIN.")
public class GtinUpdateController {

    private final GtinUpdateService gtinUpdateService;

    public GtinUpdateController(GtinUpdateService gtinUpdateService) {
        this.gtinUpdateService = gtinUpdateService;
    }

    @PutMapping("/gtin/{id}")
    @Operation(summary = "Mettre a jour un GTIN", description = "Cette operation permet de modifier un GTIN existant.")
    public ResponseEntity<Gtin> updateGtin(@PathVariable String id, @RequestBody Gtin gtin) {
        return ResponseEntity.ok(gtinUpdateService.updateGtin(id, gtin));
    }
}
