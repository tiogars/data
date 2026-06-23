package fr.tiogars.data.cave.vinnom.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.cave.vinnom.forms.VinNomImportForm;
import fr.tiogars.data.cave.vinnom.models.VinNomImportResult;
import fr.tiogars.data.cave.vinnom.services.VinNomImportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "vin-nom", description = "Operations liees a la gestion des vins.")
public class VinNomImportController {
    private final VinNomImportService vinNomImportService;
    public VinNomImportController(VinNomImportService vinNomImportService) { this.vinNomImportService = vinNomImportService; }
    @PostMapping("/vin-nom/import")
    @Operation(summary = "Importer les vins", description = "Importe des vins depuis un texte ou le format JSON historique.")
    public ResponseEntity<VinNomImportResult> importVinNoms(@RequestBody VinNomImportForm form) { return ResponseEntity.ok(vinNomImportService.importVinNoms(form)); }
}
