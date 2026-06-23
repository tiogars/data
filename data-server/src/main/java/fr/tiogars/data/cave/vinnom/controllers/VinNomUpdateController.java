package fr.tiogars.data.cave.vinnom.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.cave.vinnom.models.VinNom;
import fr.tiogars.data.cave.vinnom.services.VinNomUpdateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "vin-nom", description = "Operations liees a la gestion des vins.")
public class VinNomUpdateController {
    private final VinNomUpdateService vinNomUpdateService;
    public VinNomUpdateController(VinNomUpdateService vinNomUpdateService) { this.vinNomUpdateService = vinNomUpdateService; }
    @PutMapping("/vin-nom/{id}")
    @Operation(summary = "Mettre a jour un vin", description = "Cette operation permet de modifier un vin existant.")
    public ResponseEntity<VinNom> updateVinNom(@PathVariable String id, @RequestBody VinNom vinNom) { return ResponseEntity.ok(vinNomUpdateService.updateVinNom(id, vinNom)); }
}
