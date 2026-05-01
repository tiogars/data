package fr.tiogars.data.dev.docs.footerlink.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.dev.docs.footerlink.models.FooterLink;
import fr.tiogars.data.dev.docs.footerlink.services.FooterLinkUpdateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Tag(name = "footer-link", description = "Opérations liées à la gestion des liens de footer.")
public class FooterLinkUpdateController {

    private final FooterLinkUpdateService footerLinkUpdateService;

    public FooterLinkUpdateController(FooterLinkUpdateService footerLinkUpdateService) {
        this.footerLinkUpdateService = footerLinkUpdateService;
    }

    @PutMapping("/footer-link/{id}")
    @Operation(summary = "Modifier un lien de footer", description = "Cette opération permet de modifier un lien de footer existant.")
    public ResponseEntity<FooterLink> updateFooterLink(@PathVariable String id, @RequestBody FooterLink footerLink) {
        return ResponseEntity.ok(footerLinkUpdateService.updateFooterLink(id, footerLink));
    }
}