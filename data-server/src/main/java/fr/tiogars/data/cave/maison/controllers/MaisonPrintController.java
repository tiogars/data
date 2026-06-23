package fr.tiogars.data.cave.maison.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.cave.maison.models.MaisonPrintResponse;
import fr.tiogars.data.cave.maison.services.MaisonPrintService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "maison", description = "Operations liees a la gestion des maisons.")
public class MaisonPrintController { private final MaisonPrintService maisonPrintService; public MaisonPrintController(MaisonPrintService maisonPrintService) { this.maisonPrintService = maisonPrintService; } @GetMapping("/maison/print") @Operation(summary = "Gerer maisons", description = "Point d'entree maison.") public ResponseEntity<MaisonPrintResponse> printMaisons(@RequestParam(required = false) String mode, @RequestParam(required = false) String name) { return ResponseEntity.ok(maisonPrintService.printMaisons(mode, name)); } }
