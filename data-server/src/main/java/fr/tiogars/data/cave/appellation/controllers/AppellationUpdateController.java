package fr.tiogars.data.cave.appellation.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.cave.appellation.models.Appellation;
import fr.tiogars.data.cave.appellation.services.AppellationUpdateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "appellation", description = "Operations liees a la gestion des appellations.")
public class AppellationUpdateController {
    private final AppellationUpdateService appellationUpdateService;
    public AppellationUpdateController(AppellationUpdateService appellationUpdateService) { this.appellationUpdateService = appellationUpdateService; }
    @PutMapping("/appellation/{id}")
    @Operation(summary = "Gerer appellations", description = "Point d'entree appellation.")
    public ResponseEntity<Appellation> updateAppellation(@PathVariable String id, @RequestBody Appellation appellation) { return ResponseEntity.ok(appellationUpdateService.updateAppellation(id, appellation)); }
}
