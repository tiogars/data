package fr.tiogars.data.settings.footerlink.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.settings.footerlink.forms.FooterLinkCreationForm;
import fr.tiogars.data.settings.footerlink.models.FooterLink;
import fr.tiogars.data.settings.footerlink.services.FooterLinkCreationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "footer-link", description = "Opérations liées à la gestion des liens de footer.")
public class FooterLinkCreationController {

    private final FooterLinkCreationService footerLinkCreationService;

    public FooterLinkCreationController(FooterLinkCreationService footerLinkCreationService) {
        this.footerLinkCreationService = footerLinkCreationService;
    }

    @PostMapping("/footer-link")
    @Operation(summary = "Créer un lien de footer", description = "Cette opération permet de créer un nouveau lien de footer.")
    public ResponseEntity<FooterLink> createFooterLink(@RequestBody FooterLinkCreationForm form) {
        return ResponseEntity.ok(footerLinkCreationService.createFooterLink(form));
    }
}
