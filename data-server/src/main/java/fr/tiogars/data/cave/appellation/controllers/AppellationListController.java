package fr.tiogars.data.cave.appellation.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.cave.appellation.models.AppellationListResponse;
import fr.tiogars.data.cave.appellation.services.AppellationListService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "appellation", description = "Operations liees a la gestion des appellations.")
public class AppellationListController {
    private final AppellationListService appellationListService;
    public AppellationListController(AppellationListService appellationListService) { this.appellationListService = appellationListService; }
    @GetMapping("/appellation/list")
    @Operation(summary = "Gerer appellations", description = "Point d'entree appellation.")
    public ResponseEntity<AppellationListResponse> listAppellations() { return ResponseEntity.ok(appellationListService.listAppellations()); }
}
