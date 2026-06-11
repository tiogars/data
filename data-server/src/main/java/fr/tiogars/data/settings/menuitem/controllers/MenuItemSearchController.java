package fr.tiogars.data.settings.menuitem.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.settings.menuitem.models.MenuItemSearchResponse;
import fr.tiogars.data.settings.menuitem.services.MenuItemSearchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "menu-item", description = "Operations liees a la gestion du menu lateral.")
public class MenuItemSearchController {

    private final MenuItemSearchService menuItemSearchService;

    public MenuItemSearchController(MenuItemSearchService menuItemSearchService) {
        this.menuItemSearchService = menuItemSearchService;
    }

    @GetMapping("/menu-item/search")
    @Operation(summary = "Rechercher des entrees de menu", description = "Cette operation permet de recuperer une liste paginee d'entrees de menu, avec recherche textuelle.")
    public ResponseEntity<MenuItemSearchResponse> searchMenuItems(
        @Parameter(description = "Index de page (commence a 0).", example = "0")
        @RequestParam(defaultValue = "0") int page,
        @Parameter(description = "Nombre d'elements par page.", example = "10")
        @RequestParam(defaultValue = "10") int size,
        @Parameter(description = "Texte libre de recherche (label, chemin, icone).", example = "admin")
        @RequestParam(required = false) String q
    ) {
        if (page < 0) {
            throw new IllegalArgumentException("Le parametre page doit etre superieur ou egal a 0.");
        }

        if (size <= 0) {
            throw new IllegalArgumentException("Le parametre size doit etre superieur a 0.");
        }

        if (size > 100) {
            throw new IllegalArgumentException("Le parametre size ne peut pas depasser 100.");
        }

        return ResponseEntity.ok(menuItemSearchService.searchMenuItems(page, size, q));
    }
}
