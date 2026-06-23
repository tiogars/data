package fr.tiogars.data.cave.maison.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.cave.maison.models.MaisonListResponse;
import fr.tiogars.data.cave.maison.services.MaisonExportService;
import org.springframework.web.bind.annotation.GetMapping;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "maison", description = "Operations liees a la gestion des maisons.")
public class MaisonExportController { private final MaisonExportService maisonExportService; public MaisonExportController(MaisonExportService maisonExportService) { this.maisonExportService = maisonExportService; } @GetMapping("/maison/export") @Operation(summary = "Gerer maisons", description = "Point d'entree maison.") public ResponseEntity<MaisonListResponse> exportMaisons() { return ResponseEntity.ok(maisonExportService.exportMaisons()); } }
