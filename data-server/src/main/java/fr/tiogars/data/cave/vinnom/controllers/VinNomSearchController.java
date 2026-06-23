package fr.tiogars.data.cave.vinnom.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.cave.vinnom.models.VinNomSearchResponse;
import fr.tiogars.data.cave.vinnom.services.VinNomSearchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "vin-nom", description = "Operations liees a la gestion des vins.")
public class VinNomSearchController {
    private final VinNomSearchService vinNomSearchService;
    public VinNomSearchController(VinNomSearchService vinNomSearchService) { this.vinNomSearchService = vinNomSearchService; }
    @GetMapping("/vin-nom/search")
    @Operation(summary = "Rechercher des vins", description = "Recherche paginee.")
    public ResponseEntity<VinNomSearchResponse> searchVinNoms(@Parameter(description = "Index de page (commence a 0).", example = "0") @RequestParam(defaultValue = "0") int page, @Parameter(description = "Nombre d'elements par page.", example = "10") @RequestParam(defaultValue = "10") int size, @Parameter(description = "Texte libre de recherche (nom et identifiant de maison).", example = "cuvee") @RequestParam(required = false) String q) {
        if (page < 0) throw new IllegalArgumentException("Le parametre page doit etre superieur ou egal a 0.");
        if (size <= 0) throw new IllegalArgumentException("Le parametre size doit etre superieur a 0.");
        if (size > 100) throw new IllegalArgumentException("Le parametre size ne peut pas depasser 100.");
        return ResponseEntity.ok(vinNomSearchService.searchVinNoms(page, size, q));
    }
}
