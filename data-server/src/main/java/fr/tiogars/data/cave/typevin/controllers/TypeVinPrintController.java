package fr.tiogars.data.cave.typevin.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.cave.typevin.models.TypeVinPrintResponse;
import fr.tiogars.data.cave.typevin.services.TypeVinPrintService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "type-vin", description = "Operations liees a la gestion des types de vin.")
public class TypeVinPrintController {
    private final TypeVinPrintService typeVinPrintService;
    public TypeVinPrintController(TypeVinPrintService typeVinPrintService) { this.typeVinPrintService = typeVinPrintService; }
    @GetMapping("/type-vin/print")
    @Operation(summary = "Gerer types de vin", description = "Point d'entree type-vin.")
    public ResponseEntity<TypeVinPrintResponse> printTypeVins(@RequestParam(required = false) String mode, @RequestParam(required = false) String name) { return ResponseEntity.ok(typeVinPrintService.printTypeVins(mode, name)); }
}
