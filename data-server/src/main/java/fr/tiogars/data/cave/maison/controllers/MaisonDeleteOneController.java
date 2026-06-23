package fr.tiogars.data.cave.maison.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.cave.maison.services.MaisonDeleteOneService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "maison", description = "Operations liees a la gestion des maisons.")
public class MaisonDeleteOneController { private final MaisonDeleteOneService maisonDeleteOneService; public MaisonDeleteOneController(MaisonDeleteOneService maisonDeleteOneService) { this.maisonDeleteOneService = maisonDeleteOneService; } @DeleteMapping("/maison/{id}") @Operation(summary = "Gerer maisons", description = "Point d'entree maison.") public ResponseEntity<Void> deleteMaison(@PathVariable String id) { maisonDeleteOneService.deleteMaison(id); return ResponseEntity.noContent().build(); } }
