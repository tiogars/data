package fr.tiogars.data.softwares.android.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.softwares.android.models.AndroidPrintResponse;
import fr.tiogars.data.softwares.android.services.AndroidPrintService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "android", description = "Operations liees a la gestion des applications Android.")
public class AndroidPrintController {

    private final AndroidPrintService androidPrintService;

    public AndroidPrintController(AndroidPrintService androidPrintService) {
        this.androidPrintService = androidPrintService;
    }

    @GetMapping("/android/print")
    @Operation(summary = "Imprimer les applications Android", description = "Retourne les donnees d'impression en mode filtered ou all avec metadonnees.")
    public ResponseEntity<AndroidPrintResponse> printAndroids(
        @RequestParam(required = false) String mode,
        @RequestParam(required = false) String name,
        @RequestParam(required = false) String packageName,
        @RequestParam(required = false) String category,
        @RequestParam(required = false) String description
    ) {
        return ResponseEntity.ok(androidPrintService.printAndroids(mode, name, packageName, category, description));
    }
}