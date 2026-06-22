package fr.tiogars.data.softwares.winget.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.softwares.winget.services.WingetDeleteOneService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "winget", description = "Operations liees a la gestion des applications installables par Winget.")
public class WingetDeleteOneController {

    private final WingetDeleteOneService wingetDeleteOneService;

    public WingetDeleteOneController(WingetDeleteOneService wingetDeleteOneService) {
        this.wingetDeleteOneService = wingetDeleteOneService;
    }

    @DeleteMapping("/winget/{id}")
    @Operation(summary = "Supprimer une application Winget", description = "Cette operation permet de supprimer une application installable par Winget.")
    public ResponseEntity<Void> deleteWinget(@PathVariable String id) {
        wingetDeleteOneService.deleteWinget(id);
        return ResponseEntity.noContent().build();
    }
}
