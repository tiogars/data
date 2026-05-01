package fr.tiogars.data.dev.docs.footerlink.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.dev.docs.footerlink.models.FooterLinkListResponse;
import fr.tiogars.data.dev.docs.footerlink.services.FooterLinkListService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Tag(name = "footer-link", description = "Opérations liées à la gestion des liens de footer.")
public class FooterLinkListController {

    private final FooterLinkListService footerLinkListService;

    public FooterLinkListController(FooterLinkListService footerLinkListService) {
        this.footerLinkListService = footerLinkListService;
    }

    @GetMapping("/footer-link")
    @Operation(summary = "Lister les liens de footer", description = "Cette opération permet de récupérer la liste ordonnée des liens du footer.")
    public ResponseEntity<FooterLinkListResponse> listFooterLinks() {
        return ResponseEntity.ok(footerLinkListService.listFooterLinks());
    }
}