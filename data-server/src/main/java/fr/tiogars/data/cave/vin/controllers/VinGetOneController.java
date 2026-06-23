package fr.tiogars.data.cave.vin.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.cave.vin.models.Vin;
import fr.tiogars.data.cave.vin.services.VinGetOneService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "vin", description = "Operations liees a la gestion des vins degustes.")
public class VinGetOneController {

    private final VinGetOneService vinGetOneService;

    public VinGetOneController(VinGetOneService vinGetOneService) {
        this.vinGetOneService = vinGetOneService;
    }

    @GetMapping("/vin/{id}")
    @Operation(summary = "Récupérer un vin", description = "Cette opération permet de récupérer un vin par son identifiant.")
    public ResponseEntity<Vin> getVin(@PathVariable String id) {
        return ResponseEntity.ok(vinGetOneService.getVin(id));
    }
}
