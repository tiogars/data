package fr.tiogars.data.games.brick.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.games.brick.models.BrickState;
import fr.tiogars.data.games.brick.services.BrickImportCsvService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "brick", description = "Gestion de la collection de briques et des liens externes associes.")
public class BrickImportCsvController {

    private final BrickImportCsvService brickImportCsvService;

    public BrickImportCsvController(BrickImportCsvService brickImportCsvService) {
        this.brickImportCsvService = brickImportCsvService;
    }

    @PostMapping(value = "/brick/import/csv", consumes = { "text/csv", "text/plain" })
    @Operation(summary = "Importer les briques en CSV", description = "Importe un CSV d'etat briques/liens externes et remplace les donnees existantes.")
    public ResponseEntity<BrickState> importBricksCsv(@RequestBody(required = false) String csvContent) {
        return ResponseEntity.ok(brickImportCsvService.importStateFromCsv(csvContent));
    }
}
