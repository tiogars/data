package fr.tiogars.data.cave.vintag.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.cave.vintag.models.VinTagListResponse;
import fr.tiogars.data.cave.vintag.services.VinTagListService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "vin-tag", description = "Operations liees a la gestion des tags de vin.")
public class VinTagListController {
    private final VinTagListService vinTagListService;
    public VinTagListController(VinTagListService vinTagListService) { this.vinTagListService = vinTagListService; }
    @GetMapping("/vin-tag/list")
    @Operation(summary = "Gerer tags de vin", description = "Point d'entree vin-tag.")
    public ResponseEntity<VinTagListResponse> listVinTags() { return ResponseEntity.ok(vinTagListService.listVinTags()); }
}
