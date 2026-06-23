package fr.tiogars.data.cave.circonstance.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.cave.circonstance.models.Circonstance;
import fr.tiogars.data.cave.circonstance.services.CirconstanceUpdateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "circonstance", description = "Operations liees a la gestion des circonstances.")
public class CirconstanceUpdateController {
    private final CirconstanceUpdateService circonstanceUpdateService;
    public CirconstanceUpdateController(CirconstanceUpdateService circonstanceUpdateService) { this.circonstanceUpdateService = circonstanceUpdateService; }
    @PutMapping("/circonstance/{id}")
    @Operation(summary = "Gerer circonstances", description = "Point d'entree circonstance.")
    public ResponseEntity<Circonstance> updateCirconstance(@PathVariable String id, @RequestBody Circonstance circonstance) { return ResponseEntity.ok(circonstanceUpdateService.updateCirconstance(id, circonstance)); }
}
