package fr.tiogars.data.cave.vin.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.cave.vin.services.VinDeleteOneService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "vin", description = "Operations liees a la gestion des vins degustes.")
public class VinDeleteOneController {

    private final VinDeleteOneService vinDeleteOneService;

    public VinDeleteOneController(VinDeleteOneService vinDeleteOneService) {
        this.vinDeleteOneService = vinDeleteOneService;
    }

    @DeleteMapping("/vin/{id}")
    @Operation(summary = "Supprimer un vin", description = "Cette opération permet de supprimer un vin par son identifiant.")
    public ResponseEntity<Void> deleteVin(@PathVariable String id) {
        vinDeleteOneService.deleteVin(id);
        return ResponseEntity.noContent().build();
    }
}
