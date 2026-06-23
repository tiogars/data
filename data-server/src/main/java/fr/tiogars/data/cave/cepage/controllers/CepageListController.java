package fr.tiogars.data.cave.cepage.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.cave.cepage.models.CepageListResponse;
import fr.tiogars.data.cave.cepage.services.CepageListService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "cepage", description = "Operations liees a la gestion des cépages.")
public class CepageListController {
    private final CepageListService cepageListService;
    public CepageListController(CepageListService cepageListService) { this.cepageListService = cepageListService; }
    @GetMapping("/cepage/list")
    @Operation(summary = "Gerer cépages", description = "Point d'entree cepage.")
    public ResponseEntity<CepageListResponse> listCepages() { return ResponseEntity.ok(cepageListService.listCepages()); }
}
