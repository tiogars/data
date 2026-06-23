package fr.tiogars.data.cave.vin.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.cave.vin.models.VinPrintResponse;
import fr.tiogars.data.cave.vin.services.VinPrintService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "vin", description = "Operations liees a la gestion des vins degustes.")
public class VinPrintController {

    private final VinPrintService vinPrintService;

    public VinPrintController(VinPrintService vinPrintService) {
        this.vinPrintService = vinPrintService;
    }

    @GetMapping("/vin/print")
    @Operation(summary = "Imprimer les vins", description = "Retourne les donnees d'impression des vins en mode filtered ou all avec metadonnees.")
    public ResponseEntity<VinPrintResponse> printVins(
        @RequestParam(required = false) String mode,
        @RequestParam(required = false) Integer annee,
        @RequestParam(required = false) String region
    ) {
        return ResponseEntity.ok(vinPrintService.printVins(mode, annee, region));
    }
}
