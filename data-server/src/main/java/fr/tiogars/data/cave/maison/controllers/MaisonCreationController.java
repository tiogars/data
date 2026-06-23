package fr.tiogars.data.cave.maison.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.cave.maison.forms.MaisonCreationForm;
import fr.tiogars.data.cave.maison.models.Maison;
import fr.tiogars.data.cave.maison.services.MaisonCreationService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "maison", description = "Operations liees a la gestion des maisons.")
public class MaisonCreationController { private final MaisonCreationService maisonCreationService; public MaisonCreationController(MaisonCreationService maisonCreationService) { this.maisonCreationService = maisonCreationService; } @PostMapping("/maison") @Operation(summary = "Gerer maisons", description = "Point d'entree maison.") public ResponseEntity<Maison> createMaison(@RequestBody MaisonCreationForm form) { return ResponseEntity.ok(maisonCreationService.createMaison(form)); } }
