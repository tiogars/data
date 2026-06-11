package fr.tiogars.data.softwares.android.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.softwares.android.forms.AndroidImportForm;
import fr.tiogars.data.softwares.android.models.AndroidImportResult;
import fr.tiogars.data.softwares.android.services.AndroidImportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "android", description = "Operations liees a la gestion des applications Android.")
public class AndroidImportController {

    private final AndroidImportService androidImportService;

    public AndroidImportController(AndroidImportService androidImportService) {
        this.androidImportService = androidImportService;
    }

    @PostMapping("/android/import")
    @Operation(summary = "Importer les applications Android", description = "Importe un JSON d'applications Android et remplace les donnees existantes.")
    public ResponseEntity<AndroidImportResult> importAndroids(@RequestBody AndroidImportForm form) {
        return ResponseEntity.ok(androidImportService.importAndroids(form != null ? form.getItems() : null));
    }
}
