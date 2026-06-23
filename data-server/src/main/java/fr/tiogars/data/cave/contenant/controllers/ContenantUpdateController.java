package fr.tiogars.data.cave.contenant.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.cave.contenant.models.Contenant;
import fr.tiogars.data.cave.contenant.services.ContenantUpdateService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "contenant", description = "Operations liees a la gestion des contenants.")
public class ContenantUpdateController { private final ContenantUpdateService contenantUpdateService; public ContenantUpdateController(ContenantUpdateService contenantUpdateService) { this.contenantUpdateService = contenantUpdateService; } @PutMapping("/contenant/{id}") @Operation(summary = "Gerer contenants", description = "Point d'entree contenant.") public ResponseEntity<Contenant> updateContenant(@PathVariable String id, @RequestBody Contenant contenant) { return ResponseEntity.ok(contenantUpdateService.updateContenant(id, contenant)); } }
