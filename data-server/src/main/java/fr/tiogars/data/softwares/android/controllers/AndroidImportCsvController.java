package fr.tiogars.data.softwares.android.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.softwares.android.models.AndroidImportResult;
import fr.tiogars.data.softwares.android.services.AndroidImportCsvService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "android", description = "Operations liees a la gestion des applications Android.")
public class AndroidImportCsvController {

    private final AndroidImportCsvService androidImportCsvService;

    public AndroidImportCsvController(AndroidImportCsvService androidImportCsvService) {
        this.androidImportCsvService = androidImportCsvService;
    }

    @PostMapping(value = "/android/import/csv", consumes = { "text/csv", "text/plain" })
    @Operation(summary = "Importer les applications Android en CSV", description = "Importe des applications Android au format CSV et remplace les donnees existantes.")
    public ResponseEntity<AndroidImportResult> importAndroidsCsv(@RequestBody(required = false) String csvContent) {
        return ResponseEntity.ok(androidImportCsvService.importAndroidsFromCsv(csvContent));
    }
}
