package fr.tiogars.data.cave.vin.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.cave.vin.models.VinImportResult;
import fr.tiogars.data.cave.vin.services.VinImportCsvService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "vin", description = "Operations liees a la gestion des vins degustes.")
public class VinImportCsvController {

    private final VinImportCsvService vinImportCsvService;

    public VinImportCsvController(VinImportCsvService vinImportCsvService) {
        this.vinImportCsvService = vinImportCsvService;
    }

    @PostMapping(value = "/vin/import/csv", consumes = { "text/csv", "text/plain" })
    @Operation(summary = "Importer des vins en CSV", description = "Importe des vins depuis un CSV simple et applique les regles d'import existantes.")
    public ResponseEntity<VinImportResult> importVinsCsv(@RequestBody(required = false) String csvContent) {
        return ResponseEntity.ok(vinImportCsvService.importVinsFromCsv(csvContent));
    }
}
