package fr.tiogars.data.cave.vintag.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.cave.vintag.forms.VinTagImportForm;
import fr.tiogars.data.cave.vintag.models.VinTagImportResult;
import fr.tiogars.data.cave.vintag.services.VinTagImportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "vin-tag", description = "Operations liees a la gestion des tags de vin.")
public class VinTagImportController {
    private final VinTagImportService vinTagImportService;
    public VinTagImportController(VinTagImportService vinTagImportService) { this.vinTagImportService = vinTagImportService; }
    @PostMapping("/vin-tag/import")
    @Operation(summary = "Gerer tags de vin", description = "Point d'entree vin-tag.")
    public ResponseEntity<VinTagImportResult> importVinTags(@RequestBody VinTagImportForm form) { return ResponseEntity.ok(vinTagImportService.importVinTags(form)); }
}
