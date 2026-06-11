package fr.tiogars.data.products.gtin.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.products.gtin.models.GtinImportResult;
import fr.tiogars.data.products.gtin.services.GtinImportCsvService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "gtin", description = "Operations liees a la gestion des codes GTIN.")
public class GtinImportCsvController {

    private final GtinImportCsvService gtinImportCsvService;

    public GtinImportCsvController(GtinImportCsvService gtinImportCsvService) {
        this.gtinImportCsvService = gtinImportCsvService;
    }

    @PostMapping(value = "/gtin/import/csv", consumes = { "text/csv", "text/plain" })
    @Operation(summary = "Importer les GTIN en CSV", description = "Importe des GTIN au format CSV et remplace les donnees existantes.")
    public ResponseEntity<GtinImportResult> importGtinsCsv(@RequestBody(required = false) String csvContent) {
        return ResponseEntity.ok(gtinImportCsvService.importGtinsFromCsv(csvContent));
    }
}
