package fr.tiogars.data.settings.urlmanager.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.settings.urlmanager.models.UrlManagerState;
import fr.tiogars.data.settings.urlmanager.services.UrlManagerImportCsvService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "url-manager", description = "Gestion des URLs taguees et des cartes d'accueil.")
public class UrlManagerImportCsvController {

    private final UrlManagerImportCsvService urlManagerImportCsvService;

    public UrlManagerImportCsvController(UrlManagerImportCsvService urlManagerImportCsvService) {
        this.urlManagerImportCsvService = urlManagerImportCsvService;
    }

    @PostMapping(value = "/url-manager/import/csv", consumes = { "text/csv", "text/plain" })
    @Operation(summary = "Importer l'etat URL manager en CSV", description = "Importe l'etat URL manager au format CSV et remplace les donnees en base.")
    public ResponseEntity<UrlManagerState> importStateCsv(@RequestBody(required = false) String csvContent) {
        return ResponseEntity.ok(urlManagerImportCsvService.importStateFromCsv(csvContent));
    }
}
