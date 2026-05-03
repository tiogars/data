package fr.tiogars.data.dev.docs.menuitem.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.dev.docs.menuitem.models.MenuItemListResponse;
import fr.tiogars.data.dev.docs.menuitem.services.MenuItemListService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "menu-item", description = "Operations liees a la gestion du menu lateral.")
public class MenuItemListController {

    private final MenuItemListService menuItemListService;

    public MenuItemListController(MenuItemListService menuItemListService) {
        this.menuItemListService = menuItemListService;
    }

    @GetMapping("/menu-item")
    @Operation(summary = "Lister les entrees de menu", description = "Cette operation permet de recuperer la liste ordonnee des entrees de menu lateral.")
    public ResponseEntity<MenuItemListResponse> listMenuItems() {
        return ResponseEntity.ok(menuItemListService.listMenuItems());
    }
}
