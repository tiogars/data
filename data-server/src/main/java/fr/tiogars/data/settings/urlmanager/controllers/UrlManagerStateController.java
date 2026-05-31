package fr.tiogars.data.settings.urlmanager.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.settings.urlmanager.models.UrlManagerState;
import fr.tiogars.data.settings.urlmanager.services.UrlManagerStateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "url-manager", description = "Gestion des URLs taguees et des cartes d'accueil.")
public class UrlManagerStateController {

    private final UrlManagerStateService urlManagerStateService;

    public UrlManagerStateController(UrlManagerStateService urlManagerStateService) {
        this.urlManagerStateService = urlManagerStateService;
    }

    @GetMapping("/url-manager/state")
    @Operation(summary = "Lire l'etat URL manager", description = "Retourne les URLs taguees et les cartes configurees pour la page d'accueil.")
    public ResponseEntity<UrlManagerState> getState() {
        return ResponseEntity.ok(urlManagerStateService.getState());
    }

    @PutMapping("/url-manager/state")
    @Operation(summary = "Mettre a jour l'etat URL manager", description = "Remplace l'etat complet (URLs + cartes) avec persistance en base.")
    public ResponseEntity<UrlManagerState> updateState(@RequestBody UrlManagerState state) {
        return ResponseEntity.ok(urlManagerStateService.replaceState(state));
    }

    @GetMapping("/url-manager/export")
    @Operation(summary = "Exporter l'etat URL manager", description = "Retourne l'etat complet serialisable en JSON.")
    public ResponseEntity<UrlManagerState> exportState() {
        return ResponseEntity.ok(urlManagerStateService.getState());
    }

    @PostMapping("/url-manager/import")
    @Operation(summary = "Importer l'etat URL manager", description = "Importe un JSON d'etat complet et remplace les donnees en base.")
    public ResponseEntity<UrlManagerState> importState(@RequestBody UrlManagerState state) {
        return ResponseEntity.ok(urlManagerStateService.replaceState(state));
    }
}
