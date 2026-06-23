package fr.tiogars.data.cave.vinnom.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.cave.vinnom.services.VinNomDeleteAllService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "vin-nom", description = "Operations liees a la gestion des vins.")
public class VinNomDeleteAllController {
    private final VinNomDeleteAllService vinNomDeleteAllService;
    public VinNomDeleteAllController(VinNomDeleteAllService vinNomDeleteAllService) { this.vinNomDeleteAllService = vinNomDeleteAllService; }
    @DeleteMapping("/vin-nom")
    @Operation(summary = "Supprimer les vins", description = "Cette operation permet de supprimer les vins.")
    public ResponseEntity<Void> deleteAllVinNoms() { vinNomDeleteAllService.deleteAllVinNoms(); return ResponseEntity.noContent().build(); }
}
