package fr.tiogars.data.cave.appellation.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.cave.appellation.services.AppellationDeleteAllService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "appellation", description = "Operations liees a la gestion des appellations.")
public class AppellationDeleteAllController {
    private final AppellationDeleteAllService appellationDeleteAllService;
    public AppellationDeleteAllController(AppellationDeleteAllService appellationDeleteAllService) { this.appellationDeleteAllService = appellationDeleteAllService; }
    @DeleteMapping("/appellation")
    @Operation(summary = "Gerer appellations", description = "Point d'entree appellation.")
    public ResponseEntity<Void> deleteAllAppellations() { appellationDeleteAllService.deleteAllAppellations(); return ResponseEntity.noContent().build(); }
}
