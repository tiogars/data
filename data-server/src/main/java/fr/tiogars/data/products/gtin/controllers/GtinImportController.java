package fr.tiogars.data.products.gtin.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.products.gtin.forms.GtinImportForm;
import fr.tiogars.data.products.gtin.models.GtinImportResult;
import fr.tiogars.data.products.gtin.services.GtinImportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "gtin", description = "Operations liees a la gestion des codes GTIN.")
public class GtinImportController {

    private final GtinImportService gtinImportService;

    public GtinImportController(GtinImportService gtinImportService) {
        this.gtinImportService = gtinImportService;
    }

    @PostMapping("/gtin/import")
    @Operation(summary = "Importer les GTIN", description = "Importe un JSON de GTIN et remplace les donnees existantes.")
    public ResponseEntity<GtinImportResult> importGtins(@RequestBody GtinImportForm form) {
        return ResponseEntity.ok(gtinImportService.importGtins(form != null ? form.getItems() : null));
    }
}
