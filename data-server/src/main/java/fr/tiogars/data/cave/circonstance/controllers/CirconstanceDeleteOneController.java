package fr.tiogars.data.cave.circonstance.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.cave.circonstance.services.CirconstanceDeleteOneService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "circonstance", description = "Operations liees a la gestion des circonstances.")
public class CirconstanceDeleteOneController {
    private final CirconstanceDeleteOneService circonstanceDeleteOneService;
    public CirconstanceDeleteOneController(CirconstanceDeleteOneService circonstanceDeleteOneService) { this.circonstanceDeleteOneService = circonstanceDeleteOneService; }
    @DeleteMapping("/circonstance/{id}")
    @Operation(summary = "Gerer circonstances", description = "Point d'entree circonstance.")
    public ResponseEntity<Void> deleteCirconstance(@PathVariable String id) { circonstanceDeleteOneService.deleteCirconstance(id); return ResponseEntity.noContent().build(); }
}
