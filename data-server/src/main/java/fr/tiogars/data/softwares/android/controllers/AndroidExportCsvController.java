package fr.tiogars.data.softwares.android.controllers;

import java.nio.charset.StandardCharsets;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.softwares.android.services.AndroidExportCsvService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "android", description = "Operations liees a la gestion des applications Android.")
public class AndroidExportCsvController {

    private final AndroidExportCsvService androidExportCsvService;

    public AndroidExportCsvController(AndroidExportCsvService androidExportCsvService) {
        this.androidExportCsvService = androidExportCsvService;
    }

    @GetMapping(value = "/android/export/csv", produces = "text/csv")
    @Operation(summary = "Exporter les applications Android en CSV", description = "Retourne la liste complete des applications Android au format CSV.")
    public ResponseEntity<String> exportAndroidsCsv() {
        MediaType csvContentType = new MediaType("text", "csv", StandardCharsets.UTF_8);
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"android-export.csv\"")
            .contentType(csvContentType)
            .body(androidExportCsvService.exportAndroidsAsCsv());
    }
}
