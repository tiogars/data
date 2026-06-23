package fr.tiogars.data.cave.vin.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.cave.vin.models.VinListResponse;
import fr.tiogars.data.cave.vin.services.VinListService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "vin", description = "Operations liees a la gestion des vins degustes.")
public class VinListController {

    private final VinListService vinListService;

    public VinListController(VinListService vinListService) {
        this.vinListService = vinListService;
    }

    @GetMapping("/vin/list")
    @Operation(summary = "Lister les vins", description = "Cette opération permet de récupérer la liste des vins dégustés.")
    public ResponseEntity<VinListResponse> listVins() {
        return ResponseEntity.ok(vinListService.listVins());
    }
}
