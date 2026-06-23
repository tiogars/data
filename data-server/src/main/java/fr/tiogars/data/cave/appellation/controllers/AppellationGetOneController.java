package fr.tiogars.data.cave.appellation.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.cave.appellation.models.Appellation;
import fr.tiogars.data.cave.appellation.services.AppellationGetOneService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "appellation", description = "Operations liees a la gestion des appellations.")
public class AppellationGetOneController {
    private final AppellationGetOneService appellationGetOneService;
    public AppellationGetOneController(AppellationGetOneService appellationGetOneService) { this.appellationGetOneService = appellationGetOneService; }
    @GetMapping("/appellation/{id}")
    @Operation(summary = "Gerer appellations", description = "Point d'entree appellation.")
    public ResponseEntity<Appellation> getAppellation(@PathVariable String id) { return ResponseEntity.ok(appellationGetOneService.getAppellation(id)); }
}
