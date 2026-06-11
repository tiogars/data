package fr.tiogars.data.softwares.android.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.softwares.android.models.AndroidListResponse;
import fr.tiogars.data.softwares.android.services.AndroidExportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "android", description = "Operations liees a la gestion des applications Android.")
public class AndroidExportController {

    private final AndroidExportService androidExportService;

    public AndroidExportController(AndroidExportService androidExportService) {
        this.androidExportService = androidExportService;
    }

    @GetMapping("/android/export")
    @Operation(summary = "Exporter les applications Android", description = "Retourne la liste complete des applications Android en JSON.")
    public ResponseEntity<AndroidListResponse> exportAndroids() {
        return ResponseEntity.ok(androidExportService.exportAndroids());
    }
}
