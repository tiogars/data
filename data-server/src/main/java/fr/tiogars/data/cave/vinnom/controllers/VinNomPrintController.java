package fr.tiogars.data.cave.vinnom.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.cave.vinnom.models.VinNomPrintResponse;
import fr.tiogars.data.cave.vinnom.services.VinNomPrintService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "vin-nom", description = "Operations liees a la gestion des vins.")
public class VinNomPrintController {
    private final VinNomPrintService vinNomPrintService;
    public VinNomPrintController(VinNomPrintService vinNomPrintService) { this.vinNomPrintService = vinNomPrintService; }
    @GetMapping("/vin-nom/print")
    @Operation(summary = "Imprimer les vins", description = "Retourne les donnees d'impression en mode filtered ou all avec metadonnees.")
    public ResponseEntity<VinNomPrintResponse> printVinNoms(@RequestParam(required = false) String mode, @RequestParam(required = false) String name) { return ResponseEntity.ok(vinNomPrintService.printVinNoms(mode, name)); }
}
