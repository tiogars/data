package fr.tiogars.data.settings.menuitem.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.settings.menuitem.services.MenuItemDeleteAllService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "menu-item", description = "Operations liees a la gestion du menu lateral.")
public class MenuItemDeleteAllController {

    private final MenuItemDeleteAllService menuItemDeleteAllService;

    public MenuItemDeleteAllController(MenuItemDeleteAllService menuItemDeleteAllService) {
        this.menuItemDeleteAllService = menuItemDeleteAllService;
    }

    @DeleteMapping("/menu-item")
    @Operation(summary = "Supprimer toutes les entrees de menu", description = "Cette operation permet de supprimer toutes les entrees de menu lateral.")
    public ResponseEntity<Void> deleteAllMenuItems() {
        menuItemDeleteAllService.deleteAllMenuItems();
        return ResponseEntity.noContent().build();
    }
}
