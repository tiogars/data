package fr.tiogars.data.softwares.winget.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.softwares.winget.forms.WingetUpdateForm;
import fr.tiogars.data.softwares.winget.models.Winget;
import fr.tiogars.data.softwares.winget.services.WingetUpdateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "winget", description = "Operations liees a la gestion des applications installables par Winget.")
public class WingetUpdateController {

    private final WingetUpdateService wingetUpdateService;

    public WingetUpdateController(WingetUpdateService wingetUpdateService) {
        this.wingetUpdateService = wingetUpdateService;
    }

    @PutMapping("/winget/{id}")
    @Operation(summary = "Mettre a jour une application Winget", description = "Cette operation permet de modifier une application installable par Winget existante.")
    public ResponseEntity<Winget> updateWinget(@PathVariable String id, @RequestBody WingetUpdateForm form) {
        return ResponseEntity.ok(wingetUpdateService.updateWinget(id, form));
    }
}
