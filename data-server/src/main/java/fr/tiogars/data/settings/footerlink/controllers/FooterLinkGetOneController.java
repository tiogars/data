package fr.tiogars.data.settings.footerlink.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.settings.footerlink.models.FooterLink;
import fr.tiogars.data.settings.footerlink.services.FooterLinkGetOneService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "footer-link", description = "Opérations liées à la gestion des liens de footer.")
public class FooterLinkGetOneController {

    private final FooterLinkGetOneService footerLinkGetOneService;

    public FooterLinkGetOneController(FooterLinkGetOneService footerLinkGetOneService) {
        this.footerLinkGetOneService = footerLinkGetOneService;
    }

    @GetMapping("/footer-link/{id}")
    @Operation(summary = "Récupérer un lien de footer", description = "Cette opération permet de récupérer un lien de footer à partir de son identifiant.")
    public ResponseEntity<FooterLink> getFooterLinkById(@PathVariable String id) {
        return ResponseEntity.ok(footerLinkGetOneService.getFooterLinkById(id));
    }
}
