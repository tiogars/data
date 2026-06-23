package fr.tiogars.data.cave.vin.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.cave.vin.services.VinDeleteAllService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "vin", description = "Operations liees a la gestion des vins degustes.")
public class VinDeleteAllController {

    private final VinDeleteAllService vinDeleteAllService;

    public VinDeleteAllController(VinDeleteAllService vinDeleteAllService) {
        this.vinDeleteAllService = vinDeleteAllService;
    }

    @DeleteMapping("/vin")
    @Operation(summary = "Supprimer tous les vins", description = "Cette opération permet de supprimer tous les vins dégustés.")
    public ResponseEntity<Void> deleteAllVins() {
        vinDeleteAllService.deleteAllVins();
        return ResponseEntity.noContent().build();
    }
}
