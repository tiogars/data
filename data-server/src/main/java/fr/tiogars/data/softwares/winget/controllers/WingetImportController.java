package fr.tiogars.data.softwares.winget.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.softwares.winget.forms.WingetImportForm;
import fr.tiogars.data.softwares.winget.models.WingetImportResponse;
import fr.tiogars.data.softwares.winget.services.WingetImportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "winget", description = "Operations liees a la gestion des applications installables par Winget.")
public class WingetImportController {

    private final WingetImportService wingetImportService;

    public WingetImportController(WingetImportService wingetImportService) {
        this.wingetImportService = wingetImportService;
    }

    @PostMapping("/winget/import")
    @Operation(summary = "Importer des applications Winget", description = "Importe plusieurs applications a partir d'un wingetId par ligne.")
    public ResponseEntity<WingetImportResponse> importWingets(@RequestBody WingetImportForm form) {
        return ResponseEntity.ok(wingetImportService.importWingets(form));
    }
}
