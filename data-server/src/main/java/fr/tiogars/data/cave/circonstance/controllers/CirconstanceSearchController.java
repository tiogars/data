package fr.tiogars.data.cave.circonstance.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.cave.circonstance.models.CirconstanceSearchResponse;
import fr.tiogars.data.cave.circonstance.services.CirconstanceSearchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "circonstance", description = "Operations liees a la gestion des circonstances.")
public class CirconstanceSearchController {
    private final CirconstanceSearchService circonstanceSearchService;
    public CirconstanceSearchController(CirconstanceSearchService circonstanceSearchService) { this.circonstanceSearchService = circonstanceSearchService; }
    @GetMapping("/circonstance/search")
    @Operation(summary = "Rechercher circonstances", description = "Recherche paginee.")
    public ResponseEntity<CirconstanceSearchResponse> searchCirconstances(@Parameter(description = "Index de page (commence a 0).", example = "0") @RequestParam(defaultValue = "0") int page, @Parameter(description = "Nombre d'elements par page.", example = "10") @RequestParam(defaultValue = "10") int size, @Parameter(description = "Texte libre de recherche (nom).", example = "anniversaire") @RequestParam(required = false) String q) {
        if (page < 0) throw new IllegalArgumentException("Le parametre page doit etre superieur ou egal a 0.");
        if (size <= 0) throw new IllegalArgumentException("Le parametre size doit etre superieur a 0.");
        if (size > 100) throw new IllegalArgumentException("Le parametre size ne peut pas depasser 100.");
        return ResponseEntity.ok(circonstanceSearchService.searchCirconstances(page, size, q));
    }
}
