package fr.tiogars.data.dev.docs.brick.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.dev.docs.brick.models.Brick;
import fr.tiogars.data.dev.docs.brick.services.BrickDeleteOneService;
import fr.tiogars.data.dev.docs.brick.services.BrickGetOneService;
import fr.tiogars.data.dev.docs.brick.services.BrickUpdateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "brick", description = "Gestion de la collection de briques et des liens externes associes.")
public class BrickByIdController {

    private final BrickGetOneService brickGetOneService;
    private final BrickUpdateService brickUpdateService;
    private final BrickDeleteOneService brickDeleteOneService;

    public BrickByIdController(
        BrickGetOneService brickGetOneService,
        BrickUpdateService brickUpdateService,
        BrickDeleteOneService brickDeleteOneService
    ) {
        this.brickGetOneService = brickGetOneService;
        this.brickUpdateService = brickUpdateService;
        this.brickDeleteOneService = brickDeleteOneService;
    }

    @GetMapping("/brick/{id}")
    @Operation(summary = "Lire une brique", description = "Retourne une brique par son identifiant.")
    public ResponseEntity<Brick> getBrickById(@PathVariable String id) {
        return ResponseEntity.ok(brickGetOneService.getBrickById(id));
    }

    @PutMapping("/brick/{id}")
    @Operation(summary = "Mettre a jour une brique", description = "Met a jour les informations d'une brique existante.")
    public ResponseEntity<Brick> updateBrick(@PathVariable String id, @RequestBody Brick brick) {
        return ResponseEntity.ok(brickUpdateService.updateBrick(id, brick));
    }

    @DeleteMapping("/brick/{id}")
    @Operation(summary = "Supprimer une brique", description = "Supprime une brique par son identifiant.")
    public ResponseEntity<Void> deleteBrickById(@PathVariable String id) {
        brickDeleteOneService.deleteBrickById(id);
        return ResponseEntity.noContent().build();
    }
}
