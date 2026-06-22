package fr.tiogars.data.softwares.winget.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.softwares.winget.forms.WingetCreationForm;
import fr.tiogars.data.softwares.winget.models.Winget;
import fr.tiogars.data.softwares.winget.services.WingetCreationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "winget", description = "Operations liees a la gestion des applications installables par Winget.")
public class WingetCreationController {

    private final WingetCreationService wingetCreationService;

    public WingetCreationController(WingetCreationService wingetCreationService) {
        this.wingetCreationService = wingetCreationService;
    }

    @PostMapping("/winget")
    @Operation(summary = "Creer une application Winget", description = "Cette operation permet de creer une application installable par Winget.")
    public ResponseEntity<Winget> createWinget(@RequestBody WingetCreationForm form) {
        return ResponseEntity.ok(wingetCreationService.createWinget(form));
    }
}
