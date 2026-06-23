package fr.tiogars.data.cave.typevin.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.cave.typevin.models.TypeVinImportResult;
import fr.tiogars.data.cave.typevin.services.TypeVinImportCsvService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "type-vin", description = "Operations liees a la gestion des types de vin.")
public class TypeVinImportCsvController {
    private final TypeVinImportCsvService typeVinImportCsvService;
    public TypeVinImportCsvController(TypeVinImportCsvService typeVinImportCsvService) { this.typeVinImportCsvService = typeVinImportCsvService; }
    @PostMapping(value = "/type-vin/import/csv", consumes = { "text/csv", "text/plain" })
    @Operation(summary = "Gerer types de vin", description = "Point d'entree type-vin.")
    public ResponseEntity<TypeVinImportResult> importTypeVinsCsv(@RequestBody(required = false) String csvContent) { return ResponseEntity.ok(typeVinImportCsvService.importTypeVinsFromCsv(csvContent)); }
}
