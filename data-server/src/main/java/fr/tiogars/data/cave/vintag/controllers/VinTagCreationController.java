package fr.tiogars.data.cave.vintag.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.cave.vintag.forms.VinTagCreationForm;
import fr.tiogars.data.cave.vintag.models.VinTag;
import fr.tiogars.data.cave.vintag.services.VinTagCreationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "vin-tag", description = "Operations liees a la gestion des tags de vin.")
public class VinTagCreationController {
    private final VinTagCreationService vinTagCreationService;
    public VinTagCreationController(VinTagCreationService vinTagCreationService) { this.vinTagCreationService = vinTagCreationService; }
    @PostMapping("/vin-tag")
    @Operation(summary = "Gerer tags de vin", description = "Point d'entree vin-tag.")
    public ResponseEntity<VinTag> createVinTag(@RequestBody VinTagCreationForm form) { return ResponseEntity.ok(vinTagCreationService.createVinTag(form)); }
}
