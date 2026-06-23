package fr.tiogars.data.cave.typevin.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.cave.typevin.forms.TypeVinImportForm;
import fr.tiogars.data.cave.typevin.models.TypeVinImportResult;
import fr.tiogars.data.cave.typevin.services.TypeVinImportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "type-vin", description = "Operations liees a la gestion des types de vin.")
public class TypeVinImportController {
    private final TypeVinImportService typeVinImportService;
    public TypeVinImportController(TypeVinImportService typeVinImportService) { this.typeVinImportService = typeVinImportService; }
    @PostMapping("/type-vin/import")
    @Operation(summary = "Gerer types de vin", description = "Point d'entree type-vin.")
    public ResponseEntity<TypeVinImportResult> importTypeVins(@RequestBody TypeVinImportForm form) { return ResponseEntity.ok(typeVinImportService.importTypeVins(form)); }
}
