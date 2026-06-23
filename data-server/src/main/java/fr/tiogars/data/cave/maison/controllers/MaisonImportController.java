package fr.tiogars.data.cave.maison.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.cave.maison.forms.MaisonImportForm;
import fr.tiogars.data.cave.maison.models.MaisonImportResult;
import fr.tiogars.data.cave.maison.services.MaisonImportService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "maison", description = "Operations liees a la gestion des maisons.")
public class MaisonImportController { private final MaisonImportService maisonImportService; public MaisonImportController(MaisonImportService maisonImportService) { this.maisonImportService = maisonImportService; } @PostMapping("/maison/import") @Operation(summary = "Gerer maisons", description = "Point d'entree maison.") public ResponseEntity<MaisonImportResult> importMaisons(@RequestBody MaisonImportForm form) { return ResponseEntity.ok(maisonImportService.importMaisons(form)); } }
