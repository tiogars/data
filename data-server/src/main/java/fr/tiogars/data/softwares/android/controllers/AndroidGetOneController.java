package fr.tiogars.data.softwares.android.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.softwares.android.models.Android;
import fr.tiogars.data.softwares.android.services.AndroidGetOneService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "android", description = "Operations liees a la gestion des applications Android.")
public class AndroidGetOneController {

    private final AndroidGetOneService androidGetOneService;

    public AndroidGetOneController(AndroidGetOneService androidGetOneService) {
        this.androidGetOneService = androidGetOneService;
    }

    @GetMapping("/android/{id}")
    @Operation(summary = "Recuperer une application Android", description = "Cette operation permet de recuperer une application Android par son identifiant.")
    public ResponseEntity<Android> getAndroid(@PathVariable String id) {
        return ResponseEntity.ok(androidGetOneService.getAndroid(id));
    }
}