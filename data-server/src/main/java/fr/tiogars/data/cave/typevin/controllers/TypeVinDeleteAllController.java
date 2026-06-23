package fr.tiogars.data.cave.typevin.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.cave.typevin.services.TypeVinDeleteAllService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "type-vin", description = "Operations liees a la gestion des types de vin.")
public class TypeVinDeleteAllController {
    private final TypeVinDeleteAllService typeVinDeleteAllService;
    public TypeVinDeleteAllController(TypeVinDeleteAllService typeVinDeleteAllService) { this.typeVinDeleteAllService = typeVinDeleteAllService; }
    @DeleteMapping("/type-vin")
    @Operation(summary = "Gerer types de vin", description = "Point d'entree type-vin.")
    public ResponseEntity<Void> deleteAllTypeVins() { typeVinDeleteAllService.deleteAllTypeVins(); return ResponseEntity.noContent().build(); }
}
