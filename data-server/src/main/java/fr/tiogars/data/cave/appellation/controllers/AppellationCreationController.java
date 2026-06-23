package fr.tiogars.data.cave.appellation.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.cave.appellation.forms.AppellationCreationForm;
import fr.tiogars.data.cave.appellation.models.Appellation;
import fr.tiogars.data.cave.appellation.services.AppellationCreationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "appellation", description = "Operations liees a la gestion des appellations.")
public class AppellationCreationController {
    private final AppellationCreationService appellationCreationService;
    public AppellationCreationController(AppellationCreationService appellationCreationService) { this.appellationCreationService = appellationCreationService; }
    @PostMapping("/appellation")
    @Operation(summary = "Gerer appellations", description = "Point d'entree appellation.")
    public ResponseEntity<Appellation> createAppellation(@RequestBody AppellationCreationForm form) { return ResponseEntity.ok(appellationCreationService.createAppellation(form)); }
}
