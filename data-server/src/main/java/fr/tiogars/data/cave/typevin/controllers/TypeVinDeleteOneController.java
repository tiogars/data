package fr.tiogars.data.cave.typevin.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.cave.typevin.services.TypeVinDeleteOneService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "type-vin", description = "Operations liees a la gestion des types de vin.")
public class TypeVinDeleteOneController {
    private final TypeVinDeleteOneService typeVinDeleteOneService;
    public TypeVinDeleteOneController(TypeVinDeleteOneService typeVinDeleteOneService) { this.typeVinDeleteOneService = typeVinDeleteOneService; }
    @DeleteMapping("/type-vin/{id}")
    @Operation(summary = "Gerer types de vin", description = "Point d'entree type-vin.")
    public ResponseEntity<Void> deleteTypeVin(@PathVariable String id) { typeVinDeleteOneService.deleteTypeVin(id); return ResponseEntity.noContent().build(); }
}
