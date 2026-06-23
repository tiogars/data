package fr.tiogars.data.cave.vintag.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.cave.vintag.models.VinTagImportResult;
import fr.tiogars.data.cave.vintag.services.VinTagImportCsvService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "vin-tag", description = "Operations liees a la gestion des tags de vin.")
public class VinTagImportCsvController {
    private final VinTagImportCsvService vinTagImportCsvService;
    public VinTagImportCsvController(VinTagImportCsvService vinTagImportCsvService) { this.vinTagImportCsvService = vinTagImportCsvService; }
    @PostMapping(value = "/vin-tag/import/csv", consumes = { "text/csv", "text/plain" })
    @Operation(summary = "Gerer tags de vin", description = "Point d'entree vin-tag.")
    public ResponseEntity<VinTagImportResult> importVinTagsCsv(@RequestBody(required = false) String csvContent) { return ResponseEntity.ok(vinTagImportCsvService.importVinTagsFromCsv(csvContent)); }
}
