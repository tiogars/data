package fr.tiogars.data.cave.cepage.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.cave.cepage.models.Cepage;
import fr.tiogars.data.cave.cepage.services.CepageGetOneService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "cepage", description = "Operations liees a la gestion des cépages.")
public class CepageGetOneController {
    private final CepageGetOneService cepageGetOneService;
    public CepageGetOneController(CepageGetOneService cepageGetOneService) { this.cepageGetOneService = cepageGetOneService; }
    @GetMapping("/cepage/{id}")
    @Operation(summary = "Gerer cépages", description = "Point d'entree cepage.")
    public ResponseEntity<Cepage> getCepage(@PathVariable String id) { return ResponseEntity.ok(cepageGetOneService.getCepage(id)); }
}
