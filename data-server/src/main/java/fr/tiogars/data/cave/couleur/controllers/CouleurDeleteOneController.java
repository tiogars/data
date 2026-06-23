package fr.tiogars.data.cave.couleur.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.cave.couleur.services.CouleurDeleteOneService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "couleur", description = "Operations liees a la gestion des couleurs.")
public class CouleurDeleteOneController {
    private final CouleurDeleteOneService couleurDeleteOneService;
    public CouleurDeleteOneController(CouleurDeleteOneService couleurDeleteOneService) { this.couleurDeleteOneService = couleurDeleteOneService; }
    @DeleteMapping("/couleur/{id}")
    @Operation(summary = "Gerer couleurs", description = "Point d'entree couleur.")
    public ResponseEntity<Void> deleteCouleur(@PathVariable String id) { couleurDeleteOneService.deleteCouleur(id); return ResponseEntity.noContent().build(); }
}
