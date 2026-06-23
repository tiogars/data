package fr.tiogars.data.cave.maison.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.cave.maison.models.Maison;
import fr.tiogars.data.cave.maison.services.MaisonGetOneService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "maison", description = "Operations liees a la gestion des maisons.")
public class MaisonGetOneController { private final MaisonGetOneService maisonGetOneService; public MaisonGetOneController(MaisonGetOneService maisonGetOneService) { this.maisonGetOneService = maisonGetOneService; } @GetMapping("/maison/{id}") @Operation(summary = "Gerer maisons", description = "Point d'entree maison.") public ResponseEntity<Maison> getMaison(@PathVariable String id) { return ResponseEntity.ok(maisonGetOneService.getMaison(id)); } }
