package fr.tiogars.data.cave.maison.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.cave.maison.services.MaisonDeleteAllService;
import org.springframework.web.bind.annotation.DeleteMapping;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "maison", description = "Operations liees a la gestion des maisons.")
public class MaisonDeleteAllController { private final MaisonDeleteAllService maisonDeleteAllService; public MaisonDeleteAllController(MaisonDeleteAllService maisonDeleteAllService) { this.maisonDeleteAllService = maisonDeleteAllService; } @DeleteMapping("/maison") @Operation(summary = "Gerer maisons", description = "Point d'entree maison.") public ResponseEntity<Void> deleteAllMaisons() { maisonDeleteAllService.deleteAllMaisons(); return ResponseEntity.noContent().build(); } }
