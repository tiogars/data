package fr.tiogars.data.cave.contenant.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.cave.contenant.forms.ContenantCreationForm;
import fr.tiogars.data.cave.contenant.models.Contenant;
import fr.tiogars.data.cave.contenant.services.ContenantCreationService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "contenant", description = "Operations liees a la gestion des contenants.")
public class ContenantCreationController { private final ContenantCreationService contenantCreationService; public ContenantCreationController(ContenantCreationService contenantCreationService) { this.contenantCreationService = contenantCreationService; } @PostMapping("/contenant") @Operation(summary = "Gerer contenants", description = "Point d'entree contenant.") public ResponseEntity<Contenant> createContenant(@RequestBody ContenantCreationForm form) { return ResponseEntity.ok(contenantCreationService.createContenant(form)); } }
