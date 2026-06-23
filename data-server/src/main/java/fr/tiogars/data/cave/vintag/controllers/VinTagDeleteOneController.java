package fr.tiogars.data.cave.vintag.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.cave.vintag.services.VinTagDeleteOneService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "vin-tag", description = "Operations liees a la gestion des tags de vin.")
public class VinTagDeleteOneController {
    private final VinTagDeleteOneService vinTagDeleteOneService;
    public VinTagDeleteOneController(VinTagDeleteOneService vinTagDeleteOneService) { this.vinTagDeleteOneService = vinTagDeleteOneService; }
    @DeleteMapping("/vin-tag/{id}")
    @Operation(summary = "Gerer tags de vin", description = "Point d'entree vin-tag.")
    public ResponseEntity<Void> deleteVinTag(@PathVariable String id) { vinTagDeleteOneService.deleteVinTag(id); return ResponseEntity.noContent().build(); }
}
