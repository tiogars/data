package fr.tiogars.data.games.brick.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.games.brick.forms.BrickImportForm;
import fr.tiogars.data.games.brick.models.BrickState;
import fr.tiogars.data.games.brick.services.BrickImportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "brick", description = "Gestion de la collection de briques et des liens externes associes.")
public class BrickImportController {

    private final BrickImportService brickImportService;

    public BrickImportController(BrickImportService brickImportService) {
        this.brickImportService = brickImportService;
    }

    @PostMapping("/brick/import")
    @Operation(summary = "Importer les briques", description = "Importe un JSON complet et remplace les briques + liens externes existants.")
    public ResponseEntity<BrickState> importBricks(@RequestBody BrickImportForm form) {
        return ResponseEntity.ok(
            brickImportService.importState(
                form != null ? form.getBricks() : null,
                form != null ? form.getExternalLinks() : null
            )
        );
    }
}
