package fr.tiogars.data.settings.menuitem.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.settings.menuitem.models.MenuItem;
import fr.tiogars.data.settings.menuitem.services.MenuItemGetOneService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "menu-item", description = "Operations liees a la gestion du menu lateral.")
public class MenuItemGetOneController {

    private final MenuItemGetOneService menuItemGetOneService;

    public MenuItemGetOneController(MenuItemGetOneService menuItemGetOneService) {
        this.menuItemGetOneService = menuItemGetOneService;
    }

    @GetMapping("/menu-item/{id}")
    @Operation(summary = "Recuperer une entree de menu", description = "Cette operation permet de recuperer une entree de menu a partir de son identifiant.")
    public ResponseEntity<MenuItem> getMenuItemById(@PathVariable String id) {
        return ResponseEntity.ok(menuItemGetOneService.getMenuItemById(id));
    }
}
