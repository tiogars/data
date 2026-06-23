package fr.tiogars.data.cave.contenant.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.cave.contenant.models.Contenant;
import fr.tiogars.data.cave.contenant.services.ContenantGetOneService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "contenant", description = "Operations liees a la gestion des contenants.")
public class ContenantGetOneController { private final ContenantGetOneService contenantGetOneService; public ContenantGetOneController(ContenantGetOneService contenantGetOneService) { this.contenantGetOneService = contenantGetOneService; } @GetMapping("/contenant/{id}") @Operation(summary = "Gerer contenants", description = "Point d'entree contenant.") public ResponseEntity<Contenant> getContenant(@PathVariable String id) { return ResponseEntity.ok(contenantGetOneService.getContenant(id)); } }
