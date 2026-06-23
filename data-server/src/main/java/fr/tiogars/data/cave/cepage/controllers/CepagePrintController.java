package fr.tiogars.data.cave.cepage.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.cave.cepage.models.CepagePrintResponse;
import fr.tiogars.data.cave.cepage.services.CepagePrintService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "cepage", description = "Operations liees a la gestion des cépages.")
public class CepagePrintController {
    private final CepagePrintService cepagePrintService;
    public CepagePrintController(CepagePrintService cepagePrintService) { this.cepagePrintService = cepagePrintService; }
    @GetMapping("/cepage/print")
    @Operation(summary = "Gerer cépages", description = "Point d'entree cepage.")
    public ResponseEntity<CepagePrintResponse> printCepages(@RequestParam(required = false) String mode, @RequestParam(required = false) String name) { return ResponseEntity.ok(cepagePrintService.printCepages(mode, name)); }
}
