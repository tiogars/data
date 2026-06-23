package fr.tiogars.data.cave.circonstance.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.cave.circonstance.models.Circonstance;
import fr.tiogars.data.cave.circonstance.services.CirconstanceGetOneService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "circonstance", description = "Operations liees a la gestion des circonstances.")
public class CirconstanceGetOneController {
    private final CirconstanceGetOneService circonstanceGetOneService;
    public CirconstanceGetOneController(CirconstanceGetOneService circonstanceGetOneService) { this.circonstanceGetOneService = circonstanceGetOneService; }
    @GetMapping("/circonstance/{id}")
    @Operation(summary = "Gerer circonstances", description = "Point d'entree circonstance.")
    public ResponseEntity<Circonstance> getCirconstance(@PathVariable String id) { return ResponseEntity.ok(circonstanceGetOneService.getCirconstance(id)); }
}
