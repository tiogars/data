package fr.tiogars.data.cave.contenant.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.cave.contenant.models.ContenantImportResult;
import fr.tiogars.data.cave.contenant.services.ContenantImportCsvService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "contenant", description = "Operations liees a la gestion des contenants.")
public class ContenantImportCsvController { private final ContenantImportCsvService contenantImportCsvService; public ContenantImportCsvController(ContenantImportCsvService contenantImportCsvService) { this.contenantImportCsvService = contenantImportCsvService; } @PostMapping(value = "/contenant/import/csv", consumes = { "text/csv", "text/plain" }) @Operation(summary = "Gerer contenants", description = "Point d'entree contenant.") public ResponseEntity<ContenantImportResult> importContenantsCsv(@RequestBody(required = false) String csvContent) { return ResponseEntity.ok(contenantImportCsvService.importContenantsFromCsv(csvContent)); } }
