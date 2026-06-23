package fr.tiogars.data.cave.vin.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.cave.vin.models.VinSearchResponse;
import fr.tiogars.data.cave.vin.services.VinSearchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "vin", description = "Operations liees a la gestion des vins degustes.")
public class VinSearchController {

    private final VinSearchService vinSearchService;

    public VinSearchController(VinSearchService vinSearchService) {
        this.vinSearchService = vinSearchService;
    }

    @GetMapping("/vin/search")
    @Operation(summary = "Rechercher des vins", description = "Cette opération permet de récupérer une liste paginée de vins avec filtres principaux.")
    public ResponseEntity<VinSearchResponse> searchVins(
        @Parameter(description = "Index de page (commence a 0).", example = "0")
        @RequestParam(defaultValue = "0") int page,
        @Parameter(description = "Nombre d'elements par page.", example = "10")
        @RequestParam(defaultValue = "10") int size,
        @Parameter(description = "Texte libre de recherche.", example = "bourgogne")
        @RequestParam(required = false) String q,
        @Parameter(description = "Identifiant d'appellation.")
        @RequestParam(required = false) String appellationId,
        @Parameter(description = "Identifiant de couleur.")
        @RequestParam(required = false) String couleurId,
        @Parameter(description = "Annee exacte.", example = "2022")
        @RequestParam(required = false) Integer annee
    ) {
        if (page < 0) {
            throw new IllegalArgumentException("Le paramètre page doit être supérieur ou égal à 0.");
        }
        if (size <= 0) {
            throw new IllegalArgumentException("Le paramètre size doit être supérieur à 0.");
        }
        if (size > 100) {
            throw new IllegalArgumentException("Le paramètre size ne peut pas dépasser 100.");
        }
        return ResponseEntity.ok(vinSearchService.searchVins(page, size, q, appellationId, couleurId, annee));
    }
}
