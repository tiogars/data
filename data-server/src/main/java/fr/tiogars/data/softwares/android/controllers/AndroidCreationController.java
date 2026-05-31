package fr.tiogars.data.softwares.android.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.softwares.android.forms.AndroidCreationForm;
import fr.tiogars.data.softwares.android.models.Android;
import fr.tiogars.data.softwares.android.services.AndroidCreationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "android", description = "Operations liees a la gestion des applications Android.")
public class AndroidCreationController {

    private final AndroidCreationService androidCreationService;

    public AndroidCreationController(AndroidCreationService androidCreationService) {
        this.androidCreationService = androidCreationService;
    }

    @PostMapping("/android")
    @Operation(summary = "Creer une application Android", description = "Cette operation permet de creer une application Android.")
    public ResponseEntity<Android> createAndroid(@RequestBody AndroidCreationForm form) {
        return ResponseEntity.ok(androidCreationService.createAndroid(form));
    }
}