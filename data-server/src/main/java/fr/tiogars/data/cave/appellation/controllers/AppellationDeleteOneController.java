package fr.tiogars.data.cave.appellation.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.cave.appellation.services.AppellationDeleteOneService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "appellation", description = "Operations liees a la gestion des appellations.")
public class AppellationDeleteOneController {
    private final AppellationDeleteOneService appellationDeleteOneService;
    public AppellationDeleteOneController(AppellationDeleteOneService appellationDeleteOneService) { this.appellationDeleteOneService = appellationDeleteOneService; }
    @DeleteMapping("/appellation/{id}")
    @Operation(summary = "Gerer appellations", description = "Point d'entree appellation.")
    public ResponseEntity<Void> deleteAppellation(@PathVariable String id) { appellationDeleteOneService.deleteAppellation(id); return ResponseEntity.noContent().build(); }
}
