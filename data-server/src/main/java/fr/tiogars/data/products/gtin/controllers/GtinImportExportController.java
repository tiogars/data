package fr.tiogars.data.products.gtin.controllers;

import java.nio.charset.StandardCharsets;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.products.gtin.forms.GtinImportForm;
import fr.tiogars.data.products.gtin.models.GtinImportResult;
import fr.tiogars.data.products.gtin.models.GtinListResponse;
import fr.tiogars.data.products.gtin.services.GtinImportExportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "gtin", description = "Operations liees a la gestion des codes GTIN.")
public class GtinImportExportController {

    private final GtinImportExportService gtinImportExportService;

    public GtinImportExportController(GtinImportExportService gtinImportExportService) {
        this.gtinImportExportService = gtinImportExportService;
    }

    @GetMapping("/gtin/export")
    @Operation(summary = "Exporter les GTIN", description = "Retourne la liste complete des GTIN en JSON.")
    public ResponseEntity<GtinListResponse> exportGtins() {
        return ResponseEntity.ok(gtinImportExportService.exportGtins());
    }

    @GetMapping(value = "/gtin/export/csv", produces = "text/csv")
    @Operation(summary = "Exporter les GTIN en CSV", description = "Retourne la liste complete des GTIN au format CSV.")
    public ResponseEntity<String> exportGtinsCsv() {
        MediaType csvContentType = new MediaType("text", "csv", StandardCharsets.UTF_8);
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"gtin-export.csv\"")
            .contentType(csvContentType)
            .body(gtinImportExportService.exportGtinsAsCsv());
    }

    @PostMapping("/gtin/import")
    @Operation(summary = "Importer les GTIN", description = "Importe un JSON de GTIN et remplace les donnees existantes.")
    public ResponseEntity<GtinImportResult> importGtins(@RequestBody GtinImportForm form) {
        return ResponseEntity.ok(gtinImportExportService.importGtins(form != null ? form.getItems() : null));
    }

    @PostMapping(value = "/gtin/import/csv", consumes = { "text/csv", "text/plain" })
    @Operation(summary = "Importer les GTIN en CSV", description = "Importe des GTIN au format CSV et remplace les donnees existantes.")
    public ResponseEntity<GtinImportResult> importGtinsCsv(@RequestBody(required = false) String csvContent) {
        return ResponseEntity.ok(gtinImportExportService.importGtinsFromCsv(csvContent));
    }
}
