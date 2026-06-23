package fr.tiogars.data.cave.couleur.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.cave.couleur.models.Couleur;
import fr.tiogars.data.cave.couleur.services.CouleurUpdateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "couleur", description = "Operations liees a la gestion des couleurs.")
public class CouleurUpdateController {
    private final CouleurUpdateService couleurUpdateService;
    public CouleurUpdateController(CouleurUpdateService couleurUpdateService) { this.couleurUpdateService = couleurUpdateService; }
    @PutMapping("/couleur/{id}")
    @Operation(summary = "Gerer couleurs", description = "Point d'entree couleur.")
    public ResponseEntity<Couleur> updateCouleur(@PathVariable String id, @RequestBody Couleur couleur) { return ResponseEntity.ok(couleurUpdateService.updateCouleur(id, couleur)); }
}
