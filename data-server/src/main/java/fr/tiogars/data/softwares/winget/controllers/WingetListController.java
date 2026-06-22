package fr.tiogars.data.softwares.winget.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.softwares.winget.models.WingetListResponse;
import fr.tiogars.data.softwares.winget.services.WingetListService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "winget", description = "Operations liees a la gestion des applications installables par Winget.")
public class WingetListController {

    private final WingetListService wingetListService;

    public WingetListController(WingetListService wingetListService) {
        this.wingetListService = wingetListService;
    }

    @GetMapping("/winget/list")
    @Operation(summary = "Lister les applications Winget", description = "Cette operation permet de recuperer la liste des applications installables par Winget.")
    public ResponseEntity<WingetListResponse> listWingets() {
        return ResponseEntity.ok(wingetListService.listWingets());
    }
}
