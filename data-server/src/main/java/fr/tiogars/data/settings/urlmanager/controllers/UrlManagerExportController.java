package fr.tiogars.data.settings.urlmanager.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.settings.urlmanager.models.UrlManagerState;
import fr.tiogars.data.settings.urlmanager.services.UrlManagerExportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "url-manager", description = "Gestion des URLs taguees et des cartes d'accueil.")
public class UrlManagerExportController {

    private final UrlManagerExportService urlManagerExportService;

    public UrlManagerExportController(UrlManagerExportService urlManagerExportService) {
        this.urlManagerExportService = urlManagerExportService;
    }

    @GetMapping("/url-manager/export")
    @Operation(summary = "Exporter l'etat URL manager", description = "Retourne l'etat complet serialisable en JSON.")
    public ResponseEntity<UrlManagerState> exportState() {
        return ResponseEntity.ok(urlManagerExportService.exportState());
    }
}
