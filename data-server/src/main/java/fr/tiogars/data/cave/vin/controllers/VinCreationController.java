package fr.tiogars.data.cave.vin.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.cave.vin.forms.VinCreationForm;
import fr.tiogars.data.cave.vin.models.Vin;
import fr.tiogars.data.cave.vin.services.VinCreationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "vin", description = "Operations liees a la gestion des vins degustes.")
public class VinCreationController {

    private final VinCreationService vinCreationService;

    public VinCreationController(VinCreationService vinCreationService) {
        this.vinCreationService = vinCreationService;
    }

    @PostMapping("/vin")
    @Operation(summary = "Créer un vin", description = "Cette opération permet de créer un vin dégusté.")
    public ResponseEntity<Vin> createVin(@RequestBody VinCreationForm form) {
        return ResponseEntity.ok(vinCreationService.createVin(form));
    }
}
