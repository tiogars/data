package fr.tiogars.data.dev.docs.brick.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.dev.docs.brick.models.ExternalLink;
import fr.tiogars.data.dev.docs.brick.services.ExternalLinkDeleteOneService;
import fr.tiogars.data.dev.docs.brick.services.ExternalLinkGetOneService;
import fr.tiogars.data.dev.docs.brick.services.ExternalLinkUpdateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "brick", description = "Gestion de la collection de briques et des liens externes associes.")
public class ExternalLinkByIdController {

    private final ExternalLinkGetOneService externalLinkGetOneService;
    private final ExternalLinkUpdateService externalLinkUpdateService;
    private final ExternalLinkDeleteOneService externalLinkDeleteOneService;

    public ExternalLinkByIdController(
        ExternalLinkGetOneService externalLinkGetOneService,
        ExternalLinkUpdateService externalLinkUpdateService,
        ExternalLinkDeleteOneService externalLinkDeleteOneService
    ) {
        this.externalLinkGetOneService = externalLinkGetOneService;
        this.externalLinkUpdateService = externalLinkUpdateService;
        this.externalLinkDeleteOneService = externalLinkDeleteOneService;
    }

    @GetMapping("/brick/external-link/{id}")
    @Operation(summary = "Lire un lien externe", description = "Retourne un lien externe global par identifiant.")
    public ResponseEntity<ExternalLink> getExternalLinkById(@PathVariable String id) {
        return ResponseEntity.ok(externalLinkGetOneService.getExternalLinkById(id));
    }

    @PutMapping("/brick/external-link/{id}")
    @Operation(summary = "Mettre a jour un lien externe", description = "Met a jour un lien externe global.")
    public ResponseEntity<ExternalLink> updateExternalLink(@PathVariable String id, @RequestBody ExternalLink externalLink) {
        return ResponseEntity.ok(externalLinkUpdateService.updateExternalLink(id, externalLink));
    }

    @DeleteMapping("/brick/external-link/{id}")
    @Operation(summary = "Supprimer un lien externe", description = "Supprime un lien externe global.")
    public ResponseEntity<Void> deleteExternalLinkById(@PathVariable String id) {
        externalLinkDeleteOneService.deleteExternalLinkById(id);
        return ResponseEntity.noContent().build();
    }
}
