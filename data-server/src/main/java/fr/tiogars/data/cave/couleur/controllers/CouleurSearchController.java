package fr.tiogars.data.cave.couleur.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.cave.couleur.models.CouleurSearchResponse;
import fr.tiogars.data.cave.couleur.services.CouleurSearchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "couleur", description = "Operations liees a la gestion des couleurs.")
public class CouleurSearchController {
    private final CouleurSearchService couleurSearchService;
    public CouleurSearchController(CouleurSearchService couleurSearchService) { this.couleurSearchService = couleurSearchService; }
    @GetMapping("/couleur/search")
    @Operation(summary = "Rechercher couleurs", description = "Recherche paginee.")
    public ResponseEntity<CouleurSearchResponse> searchCouleurs(@Parameter(description = "Index de page (commence a 0).", example = "0") @RequestParam(defaultValue = "0") int page, @Parameter(description = "Nombre d'elements par page.", example = "10") @RequestParam(defaultValue = "10") int size, @Parameter(description = "Texte libre de recherche (nom).", example = "rouge") @RequestParam(required = false) String q) {
        if (page < 0) throw new IllegalArgumentException("Le parametre page doit etre superieur ou egal a 0.");
        if (size <= 0) throw new IllegalArgumentException("Le parametre size doit etre superieur a 0.");
        if (size > 100) throw new IllegalArgumentException("Le parametre size ne peut pas depasser 100.");
        return ResponseEntity.ok(couleurSearchService.searchCouleurs(page, size, q));
    }
}
