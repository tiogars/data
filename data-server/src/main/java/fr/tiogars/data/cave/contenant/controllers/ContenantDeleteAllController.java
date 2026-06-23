package fr.tiogars.data.cave.contenant.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.cave.contenant.services.ContenantDeleteAllService;
import org.springframework.web.bind.annotation.DeleteMapping;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "contenant", description = "Operations liees a la gestion des contenants.")
public class ContenantDeleteAllController { private final ContenantDeleteAllService contenantDeleteAllService; public ContenantDeleteAllController(ContenantDeleteAllService contenantDeleteAllService) { this.contenantDeleteAllService = contenantDeleteAllService; } @DeleteMapping("/contenant") @Operation(summary = "Gerer contenants", description = "Point d'entree contenant.") public ResponseEntity<Void> deleteAllContenants() { contenantDeleteAllService.deleteAllContenants(); return ResponseEntity.noContent().build(); } }
