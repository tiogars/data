package fr.tiogars.data.cave.maison.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.cave.maison.models.Maison;
import fr.tiogars.data.cave.maison.services.MaisonUpdateService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "maison", description = "Operations liees a la gestion des maisons.")
public class MaisonUpdateController { private final MaisonUpdateService maisonUpdateService; public MaisonUpdateController(MaisonUpdateService maisonUpdateService) { this.maisonUpdateService = maisonUpdateService; } @PutMapping("/maison/{id}") @Operation(summary = "Gerer maisons", description = "Point d'entree maison.") public ResponseEntity<Maison> updateMaison(@PathVariable String id, @RequestBody Maison maison) { return ResponseEntity.ok(maisonUpdateService.updateMaison(id, maison)); } }
