package fr.tiogars.data.cave.maison.controllers;

import java.nio.charset.StandardCharsets;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.cave.maison.services.MaisonExportCsvService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "maison", description = "Operations liees a la gestion des maisons.")
public class MaisonExportCsvController { private final MaisonExportCsvService maisonExportCsvService; public MaisonExportCsvController(MaisonExportCsvService maisonExportCsvService) { this.maisonExportCsvService = maisonExportCsvService; } @GetMapping(value = "/maison/export/csv", produces = "text/csv") @Operation(summary = "Exporter maisons en CSV", description = "Retourne la liste complete au format CSV.") public ResponseEntity<String> exportMaisonsCsv() { MediaType csvContentType = new MediaType("text", "csv", StandardCharsets.UTF_8); return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"maison-export.csv\"").contentType(csvContentType).body(maisonExportCsvService.exportMaisonsAsCsv()); } }
