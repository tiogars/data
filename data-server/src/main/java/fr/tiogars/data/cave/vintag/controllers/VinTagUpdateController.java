package fr.tiogars.data.cave.vintag.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.cave.vintag.models.VinTag;
import fr.tiogars.data.cave.vintag.services.VinTagUpdateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "vin-tag", description = "Operations liees a la gestion des tags de vin.")
public class VinTagUpdateController {
    private final VinTagUpdateService vinTagUpdateService;
    public VinTagUpdateController(VinTagUpdateService vinTagUpdateService) { this.vinTagUpdateService = vinTagUpdateService; }
    @PutMapping("/vin-tag/{id}")
    @Operation(summary = "Gerer tags de vin", description = "Point d'entree vin-tag.")
    public ResponseEntity<VinTag> updateVinTag(@PathVariable String id, @RequestBody VinTag vinTag) { return ResponseEntity.ok(vinTagUpdateService.updateVinTag(id, vinTag)); }
}
