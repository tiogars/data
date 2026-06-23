package fr.tiogars.data.cave.vin.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.cave.vin.forms.VinCreationForm;
import fr.tiogars.data.cave.vin.models.Vin;
import fr.tiogars.data.cave.vin.services.VinUpdateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "vin", description = "Operations liees a la gestion des vins degustes.")
public class VinUpdateController {

    private final VinUpdateService vinUpdateService;

    public VinUpdateController(VinUpdateService vinUpdateService) {
        this.vinUpdateService = vinUpdateService;
    }

    @PutMapping("/vin/{id}")
    @Operation(summary = "Mettre à jour un vin", description = "Cette opération permet de modifier un vin existant.")
    public ResponseEntity<Vin> updateVin(@PathVariable String id, @RequestBody VinCreationForm form) {
        return ResponseEntity.ok(vinUpdateService.updateVin(id, form));
    }
}
