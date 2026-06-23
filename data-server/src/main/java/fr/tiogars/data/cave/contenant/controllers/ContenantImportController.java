package fr.tiogars.data.cave.contenant.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.cave.contenant.forms.ContenantImportForm;
import fr.tiogars.data.cave.contenant.models.ContenantImportResult;
import fr.tiogars.data.cave.contenant.services.ContenantImportService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "contenant", description = "Operations liees a la gestion des contenants.")
public class ContenantImportController { private final ContenantImportService contenantImportService; public ContenantImportController(ContenantImportService contenantImportService) { this.contenantImportService = contenantImportService; } @PostMapping("/contenant/import") @Operation(summary = "Gerer contenants", description = "Point d'entree contenant.") public ResponseEntity<ContenantImportResult> importContenants(@RequestBody ContenantImportForm form) { return ResponseEntity.ok(contenantImportService.importContenants(form)); } }
