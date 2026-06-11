package fr.tiogars.data.settings.urlmanager.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.settings.urlmanager.models.UrlManagerState;
import fr.tiogars.data.settings.urlmanager.services.UrlManagerImportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "url-manager", description = "Gestion des URLs taguees et des cartes d'accueil.")
public class UrlManagerImportController {

    private final UrlManagerImportService urlManagerImportService;

    public UrlManagerImportController(UrlManagerImportService urlManagerImportService) {
        this.urlManagerImportService = urlManagerImportService;
    }

    @PostMapping("/url-manager/import")
    @Operation(summary = "Importer l'etat URL manager", description = "Importe un JSON d'etat complet et remplace les donnees en base.")
    public ResponseEntity<UrlManagerState> importState(@RequestBody UrlManagerState state) {
        return ResponseEntity.ok(urlManagerImportService.importState(state));
    }
}
