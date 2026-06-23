package fr.tiogars.data.cave.typevin.controllers;

import java.nio.charset.StandardCharsets;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.cave.typevin.services.TypeVinExportCsvService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "type-vin", description = "Operations liees a la gestion des types de vin.")
public class TypeVinExportCsvController {
    private final TypeVinExportCsvService typeVinExportCsvService;
    public TypeVinExportCsvController(TypeVinExportCsvService typeVinExportCsvService) { this.typeVinExportCsvService = typeVinExportCsvService; }
    @GetMapping(value = "/type-vin/export/csv", produces = "text/csv")
    @Operation(summary = "Exporter types de vin en CSV", description = "Retourne la liste complete au format CSV.")
    public ResponseEntity<String> exportTypeVinsCsv() {
        MediaType csvContentType = new MediaType("text", "csv", StandardCharsets.UTF_8);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"type-vin-export.csv\"").contentType(csvContentType).body(typeVinExportCsvService.exportTypeVinsAsCsv());
    }
}
