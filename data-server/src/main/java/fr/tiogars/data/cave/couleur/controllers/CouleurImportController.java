package fr.tiogars.data.cave.couleur.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.cave.couleur.forms.CouleurImportForm;
import fr.tiogars.data.cave.couleur.models.CouleurImportResult;
import fr.tiogars.data.cave.couleur.services.CouleurImportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "couleur", description = "Operations liees a la gestion des couleurs.")
public class CouleurImportController {
    private final CouleurImportService couleurImportService;
    public CouleurImportController(CouleurImportService couleurImportService) { this.couleurImportService = couleurImportService; }
    @PostMapping("/couleur/import")
    @Operation(summary = "Gerer couleurs", description = "Point d'entree couleur.")
    public ResponseEntity<CouleurImportResult> importCouleurs(@RequestBody CouleurImportForm form) { return ResponseEntity.ok(couleurImportService.importCouleurs(form)); }
}
