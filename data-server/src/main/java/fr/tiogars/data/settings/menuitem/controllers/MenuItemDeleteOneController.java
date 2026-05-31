package fr.tiogars.data.settings.menuitem.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.settings.menuitem.services.MenuItemDeleteOneService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "menu-item", description = "Operations liees a la gestion du menu lateral.")
public class MenuItemDeleteOneController {

    private final MenuItemDeleteOneService menuItemDeleteOneService;

    public MenuItemDeleteOneController(MenuItemDeleteOneService menuItemDeleteOneService) {
        this.menuItemDeleteOneService = menuItemDeleteOneService;
    }

    @DeleteMapping("/menu-item/{id}")
    @Operation(summary = "Supprimer une entree de menu", description = "Cette operation permet de supprimer une entree de menu a partir de son identifiant.")
    public ResponseEntity<Void> deleteMenuItemById(@PathVariable String id) {
        menuItemDeleteOneService.deleteMenuItemById(id);
        return ResponseEntity.noContent().build();
    }
}
