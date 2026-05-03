package fr.tiogars.data.dev.docs.menuitem.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.dev.docs.menuitem.forms.MenuItemCreationForm;
import fr.tiogars.data.dev.docs.menuitem.models.MenuItem;
import fr.tiogars.data.dev.docs.menuitem.services.MenuItemCreationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "menu-item", description = "Operations liees a la gestion du menu lateral.")
public class MenuItemCreationController {

    private final MenuItemCreationService menuItemCreationService;

    public MenuItemCreationController(MenuItemCreationService menuItemCreationService) {
        this.menuItemCreationService = menuItemCreationService;
    }

    @PostMapping("/menu-item")
    @Operation(summary = "Creer une entree de menu", description = "Cette operation permet de creer une nouvelle entree de menu lateral.")
    public ResponseEntity<MenuItem> createMenuItem(@RequestBody MenuItemCreationForm form) {
        return ResponseEntity.ok(menuItemCreationService.createMenuItem(form));
    }
}
