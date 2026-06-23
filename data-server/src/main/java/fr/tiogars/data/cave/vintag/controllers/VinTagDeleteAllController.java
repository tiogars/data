package fr.tiogars.data.cave.vintag.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.cave.vintag.services.VinTagDeleteAllService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "vin-tag", description = "Operations liees a la gestion des tags de vin.")
public class VinTagDeleteAllController {
    private final VinTagDeleteAllService vinTagDeleteAllService;
    public VinTagDeleteAllController(VinTagDeleteAllService vinTagDeleteAllService) { this.vinTagDeleteAllService = vinTagDeleteAllService; }
    @DeleteMapping("/vin-tag")
    @Operation(summary = "Gerer tags de vin", description = "Point d'entree vin-tag.")
    public ResponseEntity<Void> deleteAllVinTags() { vinTagDeleteAllService.deleteAllVinTags(); return ResponseEntity.noContent().build(); }
}
