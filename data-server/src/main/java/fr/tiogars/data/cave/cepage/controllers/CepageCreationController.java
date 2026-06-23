package fr.tiogars.data.cave.cepage.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.cave.cepage.forms.CepageCreationForm;
import fr.tiogars.data.cave.cepage.models.Cepage;
import fr.tiogars.data.cave.cepage.services.CepageCreationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "cepage", description = "Operations liees a la gestion des cépages.")
public class CepageCreationController {
    private final CepageCreationService cepageCreationService;
    public CepageCreationController(CepageCreationService cepageCreationService) { this.cepageCreationService = cepageCreationService; }
    @PostMapping("/cepage")
    @Operation(summary = "Gerer cépages", description = "Point d'entree cepage.")
    public ResponseEntity<Cepage> createCepage(@RequestBody CepageCreationForm form) { return ResponseEntity.ok(cepageCreationService.createCepage(form)); }
}
