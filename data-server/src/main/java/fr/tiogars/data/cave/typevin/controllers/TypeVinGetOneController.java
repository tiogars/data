package fr.tiogars.data.cave.typevin.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.cave.typevin.models.TypeVin;
import fr.tiogars.data.cave.typevin.services.TypeVinGetOneService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "type-vin", description = "Operations liees a la gestion des types de vin.")
public class TypeVinGetOneController {
    private final TypeVinGetOneService typeVinGetOneService;
    public TypeVinGetOneController(TypeVinGetOneService typeVinGetOneService) { this.typeVinGetOneService = typeVinGetOneService; }
    @GetMapping("/type-vin/{id}")
    @Operation(summary = "Gerer types de vin", description = "Point d'entree type-vin.")
    public ResponseEntity<TypeVin> getTypeVin(@PathVariable String id) { return ResponseEntity.ok(typeVinGetOneService.getTypeVin(id)); }
}
