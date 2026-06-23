package fr.tiogars.data.cave.typevin.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.cave.typevin.forms.TypeVinCreationForm;
import fr.tiogars.data.cave.typevin.models.TypeVin;
import fr.tiogars.data.cave.typevin.services.TypeVinCreationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "type-vin", description = "Operations liees a la gestion des types de vin.")
public class TypeVinCreationController {
    private final TypeVinCreationService typeVinCreationService;
    public TypeVinCreationController(TypeVinCreationService typeVinCreationService) { this.typeVinCreationService = typeVinCreationService; }
    @PostMapping("/type-vin")
    @Operation(summary = "Gerer types de vin", description = "Point d'entree type-vin.")
    public ResponseEntity<TypeVin> createTypeVin(@RequestBody TypeVinCreationForm form) { return ResponseEntity.ok(typeVinCreationService.createTypeVin(form)); }
}
