package fr.tiogars.data.cave.typevin.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.cave.typevin.models.TypeVinSearchResponse;
import fr.tiogars.data.cave.typevin.services.TypeVinSearchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "type-vin", description = "Operations liees a la gestion des types de vin.")
public class TypeVinSearchController {
    private final TypeVinSearchService typeVinSearchService;
    public TypeVinSearchController(TypeVinSearchService typeVinSearchService) { this.typeVinSearchService = typeVinSearchService; }
    @GetMapping("/type-vin/search")
    @Operation(summary = "Rechercher types de vin", description = "Recherche paginee.")
    public ResponseEntity<TypeVinSearchResponse> searchTypeVins(@Parameter(description = "Index de page (commence a 0).", example = "0") @RequestParam(defaultValue = "0") int page, @Parameter(description = "Nombre d'elements par page.", example = "10") @RequestParam(defaultValue = "10") int size, @Parameter(description = "Texte libre de recherche (nom).", example = "brut") @RequestParam(required = false) String q) {
        if (page < 0) throw new IllegalArgumentException("Le parametre page doit etre superieur ou egal a 0.");
        if (size <= 0) throw new IllegalArgumentException("Le parametre size doit etre superieur a 0.");
        if (size > 100) throw new IllegalArgumentException("Le parametre size ne peut pas depasser 100.");
        return ResponseEntity.ok(typeVinSearchService.searchTypeVins(page, size, q));
    }
}
