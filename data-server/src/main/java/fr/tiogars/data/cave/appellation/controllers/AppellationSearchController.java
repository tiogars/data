package fr.tiogars.data.cave.appellation.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.cave.appellation.models.AppellationSearchResponse;
import fr.tiogars.data.cave.appellation.services.AppellationSearchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "appellation", description = "Operations liees a la gestion des appellations.")
public class AppellationSearchController {
    private final AppellationSearchService appellationSearchService;
    public AppellationSearchController(AppellationSearchService appellationSearchService) { this.appellationSearchService = appellationSearchService; }
    @GetMapping("/appellation/search")
    @Operation(summary = "Rechercher appellations", description = "Recherche paginee.")
    public ResponseEntity<AppellationSearchResponse> searchAppellations(@Parameter(description = "Index de page (commence a 0).", example = "0") @RequestParam(defaultValue = "0") int page, @Parameter(description = "Nombre d'elements par page.", example = "10") @RequestParam(defaultValue = "10") int size, @Parameter(description = "Texte libre de recherche (nom).", example = "champagne") @RequestParam(required = false) String q) {
        if (page < 0) throw new IllegalArgumentException("Le parametre page doit etre superieur ou egal a 0.");
        if (size <= 0) throw new IllegalArgumentException("Le parametre size doit etre superieur a 0.");
        if (size > 100) throw new IllegalArgumentException("Le parametre size ne peut pas depasser 100.");
        return ResponseEntity.ok(appellationSearchService.searchAppellations(page, size, q));
    }
}
