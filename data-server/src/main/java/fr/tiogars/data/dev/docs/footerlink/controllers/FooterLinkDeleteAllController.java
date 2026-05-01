package fr.tiogars.data.dev.docs.footerlink.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.dev.docs.footerlink.services.FooterLinkDeleteAllService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Tag(name = "footer-link", description = "Opérations liées à la gestion des liens de footer.")
public class FooterLinkDeleteAllController {

    private final FooterLinkDeleteAllService footerLinkDeleteAllService;

    public FooterLinkDeleteAllController(FooterLinkDeleteAllService footerLinkDeleteAllService) {
        this.footerLinkDeleteAllService = footerLinkDeleteAllService;
    }

    @DeleteMapping("/footer-link")
    @Operation(summary = "Supprimer tous les liens de footer", description = "Cette opération permet de supprimer tous les liens de footer.")
    public ResponseEntity<Void> deleteAllFooterLinks() {
        footerLinkDeleteAllService.deleteAllFooterLinks();
        return ResponseEntity.noContent().build();
    }
}