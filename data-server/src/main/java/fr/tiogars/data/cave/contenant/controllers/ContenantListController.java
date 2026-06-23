package fr.tiogars.data.cave.contenant.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.cave.contenant.models.ContenantListResponse;
import fr.tiogars.data.cave.contenant.services.ContenantListService;
import org.springframework.web.bind.annotation.GetMapping;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "contenant", description = "Operations liees a la gestion des contenants.")
public class ContenantListController { private final ContenantListService contenantListService; public ContenantListController(ContenantListService contenantListService) { this.contenantListService = contenantListService; } @GetMapping("/contenant/list") @Operation(summary = "Gerer contenants", description = "Point d'entree contenant.") public ResponseEntity<ContenantListResponse> listContenants() { return ResponseEntity.ok(contenantListService.listContenants()); } }
