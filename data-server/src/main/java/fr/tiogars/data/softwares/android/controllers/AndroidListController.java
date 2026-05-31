package fr.tiogars.data.softwares.android.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.softwares.android.models.AndroidListResponse;
import fr.tiogars.data.softwares.android.services.AndroidListService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "android", description = "Operations liees a la gestion des applications Android.")
public class AndroidListController {

    private final AndroidListService androidListService;

    public AndroidListController(AndroidListService androidListService) {
        this.androidListService = androidListService;
    }

    @GetMapping("/android")
    @Operation(summary = "Lister les applications Android", description = "Cette operation permet de recuperer la liste des applications Android.")
    public ResponseEntity<AndroidListResponse> listAndroids() {
        return ResponseEntity.ok(androidListService.listAndroids());
    }
}