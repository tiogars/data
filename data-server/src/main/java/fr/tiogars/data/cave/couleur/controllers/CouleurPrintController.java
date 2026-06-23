package fr.tiogars.data.cave.couleur.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.cave.couleur.models.CouleurPrintResponse;
import fr.tiogars.data.cave.couleur.services.CouleurPrintService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "couleur", description = "Operations liees a la gestion des couleurs.")
public class CouleurPrintController {
    private final CouleurPrintService couleurPrintService;
    public CouleurPrintController(CouleurPrintService couleurPrintService) { this.couleurPrintService = couleurPrintService; }
    @GetMapping("/couleur/print")
    @Operation(summary = "Gerer couleurs", description = "Point d'entree couleur.")
    public ResponseEntity<CouleurPrintResponse> printCouleurs(@RequestParam(required = false) String mode, @RequestParam(required = false) String name) { return ResponseEntity.ok(couleurPrintService.printCouleurs(mode, name)); }
}
