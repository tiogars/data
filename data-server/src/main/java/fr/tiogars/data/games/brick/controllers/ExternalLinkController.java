package fr.tiogars.data.games.brick.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.games.brick.forms.ExternalLinkCreationForm;
import fr.tiogars.data.games.brick.models.ExternalLink;
import fr.tiogars.data.games.brick.models.ExternalLinkListResponse;
import fr.tiogars.data.games.brick.services.ExternalLinkCreationService;
import fr.tiogars.data.games.brick.services.ExternalLinkListService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "brick", description = "Gestion de la collection de briques et des liens externes associes.")
public class ExternalLinkController {

    private final ExternalLinkListService externalLinkListService;
    private final ExternalLinkCreationService externalLinkCreationService;

    public ExternalLinkController(
        ExternalLinkListService externalLinkListService,
        ExternalLinkCreationService externalLinkCreationService
    ) {
        this.externalLinkListService = externalLinkListService;
        this.externalLinkCreationService = externalLinkCreationService;
    }

    @GetMapping("/brick/external-link")
    @Operation(summary = "Lister les liens externes", description = "Retourne la liste globale des liens externes pour la recherche web.")
    public ResponseEntity<ExternalLinkListResponse> listExternalLinks() {
        return ResponseEntity.ok(externalLinkListService.listExternalLinks());
    }

    @PostMapping("/brick/external-link")
    @Operation(summary = "Creer un lien externe", description = "Ajoute un nouveau lien externe global.")
    public ResponseEntity<ExternalLink> createExternalLink(@RequestBody ExternalLinkCreationForm form) {
        return ResponseEntity.ok(externalLinkCreationService.createExternalLink(form));
    }
}
