package fr.tiogars.data.cave.contenant.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.cave.contenant.services.ContenantDeleteOneService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "contenant", description = "Operations liees a la gestion des contenants.")
public class ContenantDeleteOneController { private final ContenantDeleteOneService contenantDeleteOneService; public ContenantDeleteOneController(ContenantDeleteOneService contenantDeleteOneService) { this.contenantDeleteOneService = contenantDeleteOneService; } @DeleteMapping("/contenant/{id}") @Operation(summary = "Gerer contenants", description = "Point d'entree contenant.") public ResponseEntity<Void> deleteContenant(@PathVariable String id) { contenantDeleteOneService.deleteContenant(id); return ResponseEntity.noContent().build(); } }
