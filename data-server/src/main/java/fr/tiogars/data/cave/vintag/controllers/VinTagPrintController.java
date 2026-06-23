package fr.tiogars.data.cave.vintag.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.cave.vintag.models.VinTagPrintResponse;
import fr.tiogars.data.cave.vintag.services.VinTagPrintService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "vin-tag", description = "Operations liees a la gestion des tags de vin.")
public class VinTagPrintController {
    private final VinTagPrintService vinTagPrintService;
    public VinTagPrintController(VinTagPrintService vinTagPrintService) { this.vinTagPrintService = vinTagPrintService; }
    @GetMapping("/vin-tag/print")
    @Operation(summary = "Gerer tags de vin", description = "Point d'entree vin-tag.")
    public ResponseEntity<VinTagPrintResponse> printVinTags(@RequestParam(required = false) String mode, @RequestParam(required = false) String name) { return ResponseEntity.ok(vinTagPrintService.printVinTags(mode, name)); }
}
