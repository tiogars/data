package fr.tiogars.data.products.gtin.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.products.gtin.models.GtinPrintResponse;
import fr.tiogars.data.products.gtin.services.GtinPrintService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "gtin", description = "Operations liees a la gestion des codes GTIN.")
public class GtinPrintController {

    private final GtinPrintService gtinPrintService;

    public GtinPrintController(GtinPrintService gtinPrintService) {
        this.gtinPrintService = gtinPrintService;
    }

    @GetMapping("/gtin/print")
    @Operation(summary = "Imprimer les GTIN", description = "Retourne les donnees d'impression en mode filtered ou all avec metadonnees.")
    public ResponseEntity<GtinPrintResponse> printGtins(
        @RequestParam(required = false) String mode,
        @RequestParam(required = false) String code,
        @RequestParam(required = false) String description
    ) {
        return ResponseEntity.ok(gtinPrintService.printGtins(mode, code, description));
    }
}
