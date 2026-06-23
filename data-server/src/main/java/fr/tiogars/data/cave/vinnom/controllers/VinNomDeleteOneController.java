package fr.tiogars.data.cave.vinnom.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.cave.vinnom.services.VinNomDeleteOneService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "vin-nom", description = "Operations liees a la gestion des vins.")
public class VinNomDeleteOneController {
    private final VinNomDeleteOneService vinNomDeleteOneService;
    public VinNomDeleteOneController(VinNomDeleteOneService vinNomDeleteOneService) { this.vinNomDeleteOneService = vinNomDeleteOneService; }
    @DeleteMapping("/vin-nom/{id}")
    @Operation(summary = "Supprimer un vin", description = "Cette operation permet de supprimer un vin par son identifiant.")
    public ResponseEntity<Void> deleteVinNom(@PathVariable String id) { vinNomDeleteOneService.deleteVinNom(id); return ResponseEntity.noContent().build(); }
}
