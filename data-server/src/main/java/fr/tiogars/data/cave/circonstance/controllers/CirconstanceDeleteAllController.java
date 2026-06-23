package fr.tiogars.data.cave.circonstance.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.cave.circonstance.services.CirconstanceDeleteAllService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "circonstance", description = "Operations liees a la gestion des circonstances.")
public class CirconstanceDeleteAllController {
    private final CirconstanceDeleteAllService circonstanceDeleteAllService;
    public CirconstanceDeleteAllController(CirconstanceDeleteAllService circonstanceDeleteAllService) { this.circonstanceDeleteAllService = circonstanceDeleteAllService; }
    @DeleteMapping("/circonstance")
    @Operation(summary = "Gerer circonstances", description = "Point d'entree circonstance.")
    public ResponseEntity<Void> deleteAllCirconstances() { circonstanceDeleteAllService.deleteAllCirconstances(); return ResponseEntity.noContent().build(); }
}
