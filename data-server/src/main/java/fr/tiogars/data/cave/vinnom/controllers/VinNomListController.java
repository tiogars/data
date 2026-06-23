package fr.tiogars.data.cave.vinnom.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.cave.vinnom.models.VinNomListResponse;
import fr.tiogars.data.cave.vinnom.services.VinNomListService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "vin-nom", description = "Operations liees a la gestion des vins.")
public class VinNomListController {
    private final VinNomListService vinNomListService;
    public VinNomListController(VinNomListService vinNomListService) { this.vinNomListService = vinNomListService; }
    @GetMapping("/vin-nom/list")
    @Operation(summary = "Lister les vins", description = "Cette operation permet de recuperer la liste des vins.")
    public ResponseEntity<VinNomListResponse> listVinNoms() { return ResponseEntity.ok(vinNomListService.listVinNoms()); }
}
