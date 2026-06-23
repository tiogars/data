package fr.tiogars.data.cave.circonstance.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.cave.circonstance.forms.CirconstanceCreationForm;
import fr.tiogars.data.cave.circonstance.models.Circonstance;
import fr.tiogars.data.cave.circonstance.services.CirconstanceCreationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "circonstance", description = "Operations liees a la gestion des circonstances.")
public class CirconstanceCreationController {
    private final CirconstanceCreationService circonstanceCreationService;
    public CirconstanceCreationController(CirconstanceCreationService circonstanceCreationService) { this.circonstanceCreationService = circonstanceCreationService; }
    @PostMapping("/circonstance")
    @Operation(summary = "Gerer circonstances", description = "Point d'entree circonstance.")
    public ResponseEntity<Circonstance> createCirconstance(@RequestBody CirconstanceCreationForm form) { return ResponseEntity.ok(circonstanceCreationService.createCirconstance(form)); }
}
