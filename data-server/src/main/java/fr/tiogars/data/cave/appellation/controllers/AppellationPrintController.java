package fr.tiogars.data.cave.appellation.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.cave.appellation.models.AppellationPrintResponse;
import fr.tiogars.data.cave.appellation.services.AppellationPrintService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "appellation", description = "Operations liees a la gestion des appellations.")
public class AppellationPrintController {
    private final AppellationPrintService appellationPrintService;
    public AppellationPrintController(AppellationPrintService appellationPrintService) { this.appellationPrintService = appellationPrintService; }
    @GetMapping("/appellation/print")
    @Operation(summary = "Gerer appellations", description = "Point d'entree appellation.")
    public ResponseEntity<AppellationPrintResponse> printAppellations(@RequestParam(required = false) String mode, @RequestParam(required = false) String name) { return ResponseEntity.ok(appellationPrintService.printAppellations(mode, name)); }
}
