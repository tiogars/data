package fr.tiogars.data.cave.maison.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.cave.maison.models.MaisonSearchResponse;
import fr.tiogars.data.cave.maison.services.MaisonSearchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "maison", description = "Operations liees a la gestion des maisons.")
public class MaisonSearchController { private final MaisonSearchService maisonSearchService; public MaisonSearchController(MaisonSearchService maisonSearchService) { this.maisonSearchService = maisonSearchService; } @GetMapping("/maison/search") @Operation(summary = "Rechercher maisons", description = "Recherche paginee.") public ResponseEntity<MaisonSearchResponse> searchMaisons(@Parameter(description = "Index de page (commence a 0).", example = "0") @RequestParam(defaultValue = "0") int page, @Parameter(description = "Nombre d'elements par page.", example = "10") @RequestParam(defaultValue = "10") int size, @Parameter(description = "Texte libre de recherche.", example = "moet & chandon") @RequestParam(required = false) String q) { if (page < 0) throw new IllegalArgumentException("Le parametre page doit etre superieur ou egal a 0."); if (size <= 0) throw new IllegalArgumentException("Le parametre size doit etre superieur a 0."); if (size > 100) throw new IllegalArgumentException("Le parametre size ne peut pas depasser 100."); return ResponseEntity.ok(maisonSearchService.searchMaisons(page, size, q)); } }
