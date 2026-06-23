package fr.tiogars.data.cave.typevin.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.cave.typevin.models.TypeVin;
import fr.tiogars.data.cave.typevin.services.TypeVinUpdateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "type-vin", description = "Operations liees a la gestion des types de vin.")
public class TypeVinUpdateController {
    private final TypeVinUpdateService typeVinUpdateService;
    public TypeVinUpdateController(TypeVinUpdateService typeVinUpdateService) { this.typeVinUpdateService = typeVinUpdateService; }
    @PutMapping("/type-vin/{id}")
    @Operation(summary = "Gerer types de vin", description = "Point d'entree type-vin.")
    public ResponseEntity<TypeVin> updateTypeVin(@PathVariable String id, @RequestBody TypeVin typeVin) { return ResponseEntity.ok(typeVinUpdateService.updateTypeVin(id, typeVin)); }
}
