package fr.tiogars.data.cave.couleur.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.cave.couleur.models.CouleurListResponse;
import fr.tiogars.data.cave.couleur.services.CouleurExportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "couleur", description = "Operations liees a la gestion des couleurs.")
public class CouleurExportController {
    private final CouleurExportService couleurExportService;
    public CouleurExportController(CouleurExportService couleurExportService) { this.couleurExportService = couleurExportService; }
    @GetMapping("/couleur/export")
    @Operation(summary = "Gerer couleurs", description = "Point d'entree couleur.")
    public ResponseEntity<CouleurListResponse> exportCouleurs() { return ResponseEntity.ok(couleurExportService.exportCouleurs()); }
}
