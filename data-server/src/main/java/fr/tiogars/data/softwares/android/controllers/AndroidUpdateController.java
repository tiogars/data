package fr.tiogars.data.softwares.android.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.softwares.android.models.Android;
import fr.tiogars.data.softwares.android.services.AndroidUpdateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "android", description = "Operations liees a la gestion des applications Android.")
public class AndroidUpdateController {

    private final AndroidUpdateService androidUpdateService;

    public AndroidUpdateController(AndroidUpdateService androidUpdateService) {
        this.androidUpdateService = androidUpdateService;
    }

    @PutMapping("/android/{id}")
    @Operation(summary = "Mettre a jour une application Android", description = "Cette operation permet de modifier une application Android existante.")
    public ResponseEntity<Android> updateAndroid(@PathVariable String id, @RequestBody Android android) {
        return ResponseEntity.ok(androidUpdateService.updateAndroid(id, android));
    }
}