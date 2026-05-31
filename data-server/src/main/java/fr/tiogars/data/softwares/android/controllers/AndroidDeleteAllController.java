package fr.tiogars.data.softwares.android.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.softwares.android.services.AndroidDeleteAllService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "android", description = "Operations liees a la gestion des applications Android.")
public class AndroidDeleteAllController {

    private final AndroidDeleteAllService androidDeleteAllService;

    public AndroidDeleteAllController(AndroidDeleteAllService androidDeleteAllService) {
        this.androidDeleteAllService = androidDeleteAllService;
    }

    @DeleteMapping("/android")
    @Operation(summary = "Supprimer toutes les applications Android", description = "Cette operation permet de supprimer toutes les applications Android.")
    public ResponseEntity<Void> deleteAllAndroids() {
        androidDeleteAllService.deleteAllAndroids();
        return ResponseEntity.noContent().build();
    }
}