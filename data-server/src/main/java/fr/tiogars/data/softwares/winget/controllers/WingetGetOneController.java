package fr.tiogars.data.softwares.winget.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.softwares.winget.models.Winget;
import fr.tiogars.data.softwares.winget.services.WingetGetOneService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "winget", description = "Operations liees a la gestion des applications installables par Winget.")
public class WingetGetOneController {

    private final WingetGetOneService wingetGetOneService;

    public WingetGetOneController(WingetGetOneService wingetGetOneService) {
        this.wingetGetOneService = wingetGetOneService;
    }

    @GetMapping("/winget/{id}")
    @Operation(summary = "Recuperer une application Winget", description = "Cette operation permet de recuperer une application installable par Winget par son identifiant.")
    public ResponseEntity<Winget> getWinget(@PathVariable String id) {
        return ResponseEntity.ok(wingetGetOneService.getWinget(id));
    }
}
