package fr.tiogars.data.dev.docs.brick.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.dev.docs.brick.forms.BrickCreationForm;
import fr.tiogars.data.dev.docs.brick.models.Brick;
import fr.tiogars.data.dev.docs.brick.models.BrickListResponse;
import fr.tiogars.data.dev.docs.brick.services.BrickCreationService;
import fr.tiogars.data.dev.docs.brick.services.BrickDeleteAllService;
import fr.tiogars.data.dev.docs.brick.services.BrickListService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "brick", description = "Gestion de la collection de briques et des liens externes associes.")
public class BrickController {

    private final BrickListService brickListService;
    private final BrickCreationService brickCreationService;
    private final BrickDeleteAllService brickDeleteAllService;

    public BrickController(
        BrickListService brickListService,
        BrickCreationService brickCreationService,
        BrickDeleteAllService brickDeleteAllService
    ) {
        this.brickListService = brickListService;
        this.brickCreationService = brickCreationService;
        this.brickDeleteAllService = brickDeleteAllService;
    }

    @GetMapping("/brick")
    @Operation(summary = "Lister les briques", description = "Retourne la collection complete de briques.")
    public ResponseEntity<BrickListResponse> listBricks() {
        return ResponseEntity.ok(brickListService.listBricks());
    }

    @PostMapping("/brick")
    @Operation(summary = "Creer une brique", description = "Ajoute une nouvelle brique a la collection.")
    public ResponseEntity<Brick> createBrick(@RequestBody BrickCreationForm form) {
        return ResponseEntity.ok(brickCreationService.createBrick(form));
    }

    @DeleteMapping("/brick")
    @Operation(summary = "Supprimer toutes les briques", description = "Supprime l'ensemble des briques de la collection.")
    public ResponseEntity<Void> deleteAllBricks() {
        brickDeleteAllService.deleteAllBricks();
        return ResponseEntity.noContent().build();
    }
}
