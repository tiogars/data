package fr.tiogars.data.cave.couleur.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.cave.couleur.models.CouleurImportResult;
import fr.tiogars.data.cave.couleur.services.CouleurImportCsvService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "couleur", description = "Operations liees a la gestion des couleurs.")
public class CouleurImportCsvController {
    private final CouleurImportCsvService couleurImportCsvService;
    public CouleurImportCsvController(CouleurImportCsvService couleurImportCsvService) { this.couleurImportCsvService = couleurImportCsvService; }
    @PostMapping(value = "/couleur/import/csv", consumes = { "text/csv", "text/plain" })
    @Operation(summary = "Gerer couleurs", description = "Point d'entree couleur.")
    public ResponseEntity<CouleurImportResult> importCouleursCsv(@RequestBody(required = false) String csvContent) { return ResponseEntity.ok(couleurImportCsvService.importCouleursFromCsv(csvContent)); }
}
