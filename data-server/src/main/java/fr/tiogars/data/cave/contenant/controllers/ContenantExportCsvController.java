package fr.tiogars.data.cave.contenant.controllers;

import java.nio.charset.StandardCharsets;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.cave.contenant.services.ContenantExportCsvService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "contenant", description = "Operations liees a la gestion des contenants.")
public class ContenantExportCsvController { private final ContenantExportCsvService contenantExportCsvService; public ContenantExportCsvController(ContenantExportCsvService contenantExportCsvService) { this.contenantExportCsvService = contenantExportCsvService; } @GetMapping(value = "/contenant/export/csv", produces = "text/csv") @Operation(summary = "Exporter contenants en CSV", description = "Retourne la liste complete au format CSV.") public ResponseEntity<String> exportContenantsCsv() { MediaType csvContentType = new MediaType("text", "csv", StandardCharsets.UTF_8); return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"contenant-export.csv\"").contentType(csvContentType).body(contenantExportCsvService.exportContenantsAsCsv()); } }
