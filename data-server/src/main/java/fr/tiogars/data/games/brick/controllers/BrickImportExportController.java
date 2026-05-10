package fr.tiogars.data.games.brick.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.games.brick.forms.BrickImportForm;
import fr.tiogars.data.games.brick.models.BrickState;
import fr.tiogars.data.games.brick.services.BrickImportExportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "brick", description = "Gestion de la collection de briques et des liens externes associes.")
public class BrickImportExportController {

    private final BrickImportExportService brickImportExportService;

    public BrickImportExportController(BrickImportExportService brickImportExportService) {
        this.brickImportExportService = brickImportExportService;
    }

    @GetMapping("/brick/export")
    @Operation(summary = "Exporter les briques", description = "Exporte les briques et les liens externes dans un JSON complet.")
    public ResponseEntity<BrickState> exportBricks() {
        return ResponseEntity.ok(brickImportExportService.exportState());
    }

    @PostMapping("/brick/import")
    @Operation(summary = "Importer les briques", description = "Importe un JSON complet et remplace les briques + liens externes existants.")
    public ResponseEntity<BrickState> importBricks(@RequestBody BrickImportForm form) {
        return ResponseEntity.ok(
            brickImportExportService.importState(
                form != null ? form.getBricks() : null,
                form != null ? form.getExternalLinks() : null
            )
        );
    }
}
