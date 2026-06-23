package fr.tiogars.data.cave.vin.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.cave.vin.forms.VinImportForm;
import fr.tiogars.data.cave.vin.models.VinImportResult;
import fr.tiogars.data.cave.vin.services.VinImportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "vin", description = "Operations liees a la gestion des vins degustes.")
public class VinImportController {

    private final VinImportService vinImportService;

    public VinImportController(VinImportService vinImportService) {
        this.vinImportService = vinImportService;
    }

    @PostMapping("/vin/import")
    @Operation(summary = "Importer des vins", description = "Importe une liste JSON de vins.")
    public ResponseEntity<VinImportResult> importVins(@RequestBody VinImportForm form) {
        return ResponseEntity.ok(vinImportService.importVins(form));
    }
}
