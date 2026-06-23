package fr.tiogars.data.cave.maison.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.cave.maison.models.MaisonImportResult;
import fr.tiogars.data.cave.maison.services.MaisonImportCsvService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "maison", description = "Operations liees a la gestion des maisons.")
public class MaisonImportCsvController { private final MaisonImportCsvService maisonImportCsvService; public MaisonImportCsvController(MaisonImportCsvService maisonImportCsvService) { this.maisonImportCsvService = maisonImportCsvService; } @PostMapping(value = "/maison/import/csv", consumes = { "text/csv", "text/plain" }) @Operation(summary = "Gerer maisons", description = "Point d'entree maison.") public ResponseEntity<MaisonImportResult> importMaisonsCsv(@RequestBody(required = false) String csvContent) { return ResponseEntity.ok(maisonImportCsvService.importMaisonsFromCsv(csvContent)); } }
