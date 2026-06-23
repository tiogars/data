package fr.tiogars.data.cave.typevin.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.cave.typevin.models.TypeVinListResponse;
import fr.tiogars.data.cave.typevin.services.TypeVinListService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "type-vin", description = "Operations liees a la gestion des types de vin.")
public class TypeVinListController {
    private final TypeVinListService typeVinListService;
    public TypeVinListController(TypeVinListService typeVinListService) { this.typeVinListService = typeVinListService; }
    @GetMapping("/type-vin/list")
    @Operation(summary = "Gerer types de vin", description = "Point d'entree type-vin.")
    public ResponseEntity<TypeVinListResponse> listTypeVins() { return ResponseEntity.ok(typeVinListService.listTypeVins()); }
}
