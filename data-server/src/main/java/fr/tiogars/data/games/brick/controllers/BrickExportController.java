package fr.tiogars.data.games.brick.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.games.brick.models.BrickState;
import fr.tiogars.data.games.brick.services.BrickExportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "brick", description = "Gestion de la collection de briques et des liens externes associes.")
public class BrickExportController {

    private final BrickExportService brickExportService;

    public BrickExportController(BrickExportService brickExportService) {
        this.brickExportService = brickExportService;
    }

    @GetMapping("/brick/export")
    @Operation(summary = "Exporter les briques", description = "Exporte les briques et les liens externes dans un JSON complet.")
    public ResponseEntity<BrickState> exportBricks() {
        return ResponseEntity.ok(brickExportService.exportState());
    }
}
