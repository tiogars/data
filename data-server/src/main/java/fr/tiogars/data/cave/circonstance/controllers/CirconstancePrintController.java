package fr.tiogars.data.cave.circonstance.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.cave.circonstance.models.CirconstancePrintResponse;
import fr.tiogars.data.cave.circonstance.services.CirconstancePrintService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "circonstance", description = "Operations liees a la gestion des circonstances.")
public class CirconstancePrintController {
    private final CirconstancePrintService circonstancePrintService;
    public CirconstancePrintController(CirconstancePrintService circonstancePrintService) { this.circonstancePrintService = circonstancePrintService; }
    @GetMapping("/circonstance/print")
    @Operation(summary = "Gerer circonstances", description = "Point d'entree circonstance.")
    public ResponseEntity<CirconstancePrintResponse> printCirconstances(@RequestParam(required = false) String mode, @RequestParam(required = false) String name) { return ResponseEntity.ok(circonstancePrintService.printCirconstances(mode, name)); }
}
