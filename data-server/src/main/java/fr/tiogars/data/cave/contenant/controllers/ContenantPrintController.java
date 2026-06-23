package fr.tiogars.data.cave.contenant.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.cave.contenant.models.ContenantPrintResponse;
import fr.tiogars.data.cave.contenant.services.ContenantPrintService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "contenant", description = "Operations liees a la gestion des contenants.")
public class ContenantPrintController { private final ContenantPrintService contenantPrintService; public ContenantPrintController(ContenantPrintService contenantPrintService) { this.contenantPrintService = contenantPrintService; } @GetMapping("/contenant/print") @Operation(summary = "Gerer contenants", description = "Point d'entree contenant.") public ResponseEntity<ContenantPrintResponse> printContenants(@RequestParam(required = false) String mode, @RequestParam(required = false) String name) { return ResponseEntity.ok(contenantPrintService.printContenants(mode, name)); } }
