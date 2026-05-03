package fr.tiogars.data.dev.docs.gtin.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.dev.docs.gtin.forms.GtinCreationForm;
import fr.tiogars.data.dev.docs.gtin.models.Gtin;
import fr.tiogars.data.dev.docs.gtin.services.GtinCreationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "gtin", description = "Operations liees a la gestion des codes GTIN.")
public class GtinCreationController {

    private final GtinCreationService gtinCreationService;

    public GtinCreationController(GtinCreationService gtinCreationService) {
        this.gtinCreationService = gtinCreationService;
    }

    @PostMapping("/gtin")
    @Operation(summary = "Creer un GTIN", description = "Cette operation permet de creer un GTIN.")
    public ResponseEntity<Gtin> createGtin(@RequestBody GtinCreationForm form) {
        return ResponseEntity.ok(gtinCreationService.createGtin(form));
    }
}
