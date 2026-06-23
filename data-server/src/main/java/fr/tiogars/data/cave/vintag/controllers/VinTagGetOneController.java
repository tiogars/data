package fr.tiogars.data.cave.vintag.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.cave.vintag.models.VinTag;
import fr.tiogars.data.cave.vintag.services.VinTagGetOneService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "vin-tag", description = "Operations liees a la gestion des tags de vin.")
public class VinTagGetOneController {
    private final VinTagGetOneService vinTagGetOneService;
    public VinTagGetOneController(VinTagGetOneService vinTagGetOneService) { this.vinTagGetOneService = vinTagGetOneService; }
    @GetMapping("/vin-tag/{id}")
    @Operation(summary = "Gerer tags de vin", description = "Point d'entree vin-tag.")
    public ResponseEntity<VinTag> getVinTag(@PathVariable String id) { return ResponseEntity.ok(vinTagGetOneService.getVinTag(id)); }
}
