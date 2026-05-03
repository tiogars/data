package fr.tiogars.data.dev.docs.gtin.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.dev.docs.gtin.models.Gtin;
import fr.tiogars.data.dev.docs.gtin.services.GtinGetOneService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "gtin", description = "Operations liees a la gestion des codes GTIN.")
public class GtinGetOneController {

    private final GtinGetOneService gtinGetOneService;

    public GtinGetOneController(GtinGetOneService gtinGetOneService) {
        this.gtinGetOneService = gtinGetOneService;
    }

    @GetMapping("/gtin/{id}")
    @Operation(summary = "Recuperer un GTIN", description = "Cette operation permet de recuperer un GTIN par son identifiant.")
    public ResponseEntity<Gtin> getGtin(@PathVariable String id) {
        return ResponseEntity.ok(gtinGetOneService.getGtin(id));
    }
}
