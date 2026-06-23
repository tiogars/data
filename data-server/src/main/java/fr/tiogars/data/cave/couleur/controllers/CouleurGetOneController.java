package fr.tiogars.data.cave.couleur.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.cave.couleur.models.Couleur;
import fr.tiogars.data.cave.couleur.services.CouleurGetOneService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "couleur", description = "Operations liees a la gestion des couleurs.")
public class CouleurGetOneController {
    private final CouleurGetOneService couleurGetOneService;
    public CouleurGetOneController(CouleurGetOneService couleurGetOneService) { this.couleurGetOneService = couleurGetOneService; }
    @GetMapping("/couleur/{id}")
    @Operation(summary = "Gerer couleurs", description = "Point d'entree couleur.")
    public ResponseEntity<Couleur> getCouleur(@PathVariable String id) { return ResponseEntity.ok(couleurGetOneService.getCouleur(id)); }
}
