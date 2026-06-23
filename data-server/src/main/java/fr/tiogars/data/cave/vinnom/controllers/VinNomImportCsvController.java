package fr.tiogars.data.cave.vinnom.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.cave.vinnom.models.VinNomImportResult;
import fr.tiogars.data.cave.vinnom.services.VinNomImportCsvService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "vin-nom", description = "Operations liees a la gestion des vins.")
public class VinNomImportCsvController {
    private final VinNomImportCsvService vinNomImportCsvService;
    public VinNomImportCsvController(VinNomImportCsvService vinNomImportCsvService) { this.vinNomImportCsvService = vinNomImportCsvService; }
    @PostMapping(value = "/vin-nom/import/csv", consumes = { "text/csv", "text/plain" })
    @Operation(summary = "Importer les vins en CSV", description = "Importe des vins au format CSV et applique les regles d'import existantes.")
    public ResponseEntity<VinNomImportResult> importVinNomsCsv(@RequestBody(required = false) String csvContent) { return ResponseEntity.ok(vinNomImportCsvService.importVinNomsFromCsv(csvContent)); }
}
