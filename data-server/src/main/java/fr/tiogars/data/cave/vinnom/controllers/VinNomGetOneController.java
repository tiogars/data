package fr.tiogars.data.cave.vinnom.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.cave.vinnom.models.VinNom;
import fr.tiogars.data.cave.vinnom.services.VinNomGetOneService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "vin-nom", description = "Operations liees a la gestion des vins.")
public class VinNomGetOneController {
    private final VinNomGetOneService vinNomGetOneService;
    public VinNomGetOneController(VinNomGetOneService vinNomGetOneService) { this.vinNomGetOneService = vinNomGetOneService; }
    @GetMapping("/vin-nom/{id}")
    @Operation(summary = "Recuperer un vin", description = "Cette operation permet de recuperer un vin par son identifiant.")
    public ResponseEntity<VinNom> getVinNom(@PathVariable String id) { return ResponseEntity.ok(vinNomGetOneService.getVinNom(id)); }
}
