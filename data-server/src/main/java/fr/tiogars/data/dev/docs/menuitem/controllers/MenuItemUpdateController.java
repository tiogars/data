package fr.tiogars.data.dev.docs.menuitem.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.dev.docs.menuitem.models.MenuItem;
import fr.tiogars.data.dev.docs.menuitem.services.MenuItemUpdateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "menu-item", description = "Operations liees a la gestion du menu lateral.")
public class MenuItemUpdateController {

    private final MenuItemUpdateService menuItemUpdateService;

    public MenuItemUpdateController(MenuItemUpdateService menuItemUpdateService) {
        this.menuItemUpdateService = menuItemUpdateService;
    }

    @PutMapping("/menu-item/{id}")
    @Operation(summary = "Modifier une entree de menu", description = "Cette operation permet de modifier une entree de menu existante.")
    public ResponseEntity<MenuItem> updateMenuItem(@PathVariable String id, @RequestBody MenuItem menuItem) {
        return ResponseEntity.ok(menuItemUpdateService.updateMenuItem(id, menuItem));
    }
}
