package fr.tiogars.data.cave.couleur.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.cave.couleur.forms.CouleurCreationForm;
import fr.tiogars.data.cave.couleur.models.Couleur;
import fr.tiogars.data.cave.couleur.services.CouleurCreationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "couleur", description = "Operations liees a la gestion des couleurs.")
public class CouleurCreationController {
    private final CouleurCreationService couleurCreationService;
    public CouleurCreationController(CouleurCreationService couleurCreationService) { this.couleurCreationService = couleurCreationService; }
    @PostMapping("/couleur")
    @Operation(summary = "Gerer couleurs", description = "Point d'entree couleur.")
    public ResponseEntity<Couleur> createCouleur(@RequestBody CouleurCreationForm form) { return ResponseEntity.ok(couleurCreationService.createCouleur(form)); }
}
