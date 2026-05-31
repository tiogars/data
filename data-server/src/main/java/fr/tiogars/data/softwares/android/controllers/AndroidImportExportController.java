package fr.tiogars.data.softwares.android.controllers;

import java.nio.charset.StandardCharsets;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.softwares.android.forms.AndroidImportForm;
import fr.tiogars.data.softwares.android.models.AndroidImportResult;
import fr.tiogars.data.softwares.android.models.AndroidListResponse;
import fr.tiogars.data.softwares.android.services.AndroidImportExportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "android", description = "Operations liees a la gestion des applications Android.")
public class AndroidImportExportController {

    private final AndroidImportExportService androidImportExportService;

    public AndroidImportExportController(AndroidImportExportService androidImportExportService) {
        this.androidImportExportService = androidImportExportService;
    }

    @GetMapping("/android/export")
    @Operation(summary = "Exporter les applications Android", description = "Retourne la liste complete des applications Android en JSON.")
    public ResponseEntity<AndroidListResponse> exportAndroids() {
        return ResponseEntity.ok(androidImportExportService.exportAndroids());
    }

    @GetMapping(value = "/android/export/csv", produces = "text/csv")
    @Operation(summary = "Exporter les applications Android en CSV", description = "Retourne la liste complete des applications Android au format CSV.")
    public ResponseEntity<String> exportAndroidsCsv() {
        MediaType csvContentType = new MediaType("text", "csv", StandardCharsets.UTF_8);
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"android-export.csv\"")
            .contentType(csvContentType)
            .body(androidImportExportService.exportAndroidsAsCsv());
    }

    @PostMapping("/android/import")
    @Operation(summary = "Importer les applications Android", description = "Importe un JSON d'applications Android et remplace les donnees existantes.")
    public ResponseEntity<AndroidImportResult> importAndroids(@RequestBody AndroidImportForm form) {
        return ResponseEntity.ok(androidImportExportService.importAndroids(form != null ? form.getItems() : null));
    }

    @PostMapping(value = "/android/import/csv", consumes = { "text/csv", "text/plain" })
    @Operation(summary = "Importer les applications Android en CSV", description = "Importe des applications Android au format CSV et remplace les donnees existantes.")
    public ResponseEntity<AndroidImportResult> importAndroidsCsv(@RequestBody(required = false) String csvContent) {
        return ResponseEntity.ok(androidImportExportService.importAndroidsFromCsv(csvContent));
    }
}