package fr.tiogars.data.softwares.android.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.softwares.android.services.AndroidDeleteOneService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "android", description = "Operations liees a la gestion des applications Android.")
public class AndroidDeleteOneController {

    private final AndroidDeleteOneService androidDeleteOneService;

    public AndroidDeleteOneController(AndroidDeleteOneService androidDeleteOneService) {
        this.androidDeleteOneService = androidDeleteOneService;
    }

    @DeleteMapping("/android/{id}")
    @Operation(summary = "Supprimer une application Android", description = "Cette operation permet de supprimer une application Android.")
    public ResponseEntity<Void> deleteAndroid(@PathVariable String id) {
        androidDeleteOneService.deleteAndroid(id);
        return ResponseEntity.noContent().build();
    }
}