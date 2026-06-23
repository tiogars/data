package fr.tiogars.data.cave.vinnom.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.cave.vinnom.forms.VinNomCreationForm;
import fr.tiogars.data.cave.vinnom.models.VinNom;
import fr.tiogars.data.cave.vinnom.services.VinNomCreationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "vin-nom", description = "Operations liees a la gestion des vins.")
public class VinNomCreationController {
    private final VinNomCreationService vinNomCreationService;
    public VinNomCreationController(VinNomCreationService vinNomCreationService) { this.vinNomCreationService = vinNomCreationService; }
    @PostMapping("/vin-nom")
    @Operation(summary = "Creer un vin", description = "Cette operation permet de creer un vin.")
    public ResponseEntity<VinNom> createVinNom(@RequestBody VinNomCreationForm form) { return ResponseEntity.ok(vinNomCreationService.createVinNom(form)); }
}
