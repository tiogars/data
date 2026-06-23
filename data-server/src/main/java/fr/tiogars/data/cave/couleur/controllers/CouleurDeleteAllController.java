package fr.tiogars.data.cave.couleur.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.cave.couleur.services.CouleurDeleteAllService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "couleur", description = "Operations liees a la gestion des couleurs.")
public class CouleurDeleteAllController {
    private final CouleurDeleteAllService couleurDeleteAllService;
    public CouleurDeleteAllController(CouleurDeleteAllService couleurDeleteAllService) { this.couleurDeleteAllService = couleurDeleteAllService; }
    @DeleteMapping("/couleur")
    @Operation(summary = "Gerer couleurs", description = "Point d'entree couleur.")
    public ResponseEntity<Void> deleteAllCouleurs() { couleurDeleteAllService.deleteAllCouleurs(); return ResponseEntity.noContent().build(); }
}
