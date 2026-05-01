package fr.tiogars.data.dev.docs.footerlink.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.dev.docs.footerlink.services.FooterLinkDeleteOneService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Tag(name = "footer-link", description = "Opérations liées à la gestion des liens de footer.")
public class FooterLinkDeleteOneController {

    private final FooterLinkDeleteOneService footerLinkDeleteOneService;

    public FooterLinkDeleteOneController(FooterLinkDeleteOneService footerLinkDeleteOneService) {
        this.footerLinkDeleteOneService = footerLinkDeleteOneService;
    }

    @DeleteMapping("/footer-link/{id}")
    @Operation(summary = "Supprimer un lien de footer", description = "Cette opération permet de supprimer un lien de footer à partir de son identifiant.")
    public ResponseEntity<Void> deleteFooterLinkById(@PathVariable String id) {
        footerLinkDeleteOneService.deleteFooterLinkById(id);
        return ResponseEntity.noContent().build();
    }
}