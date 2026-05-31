package fr.tiogars.data.settings.menuitem.services;

import java.util.List;

import org.springframework.stereotype.Service;

import fr.tiogars.data.settings.menuitem.entities.MenuItemEntity;
import fr.tiogars.data.settings.menuitem.models.MenuItemListResponse;
import fr.tiogars.data.settings.menuitem.repositories.MenuItemRepository;

@Service
public class MenuItemListService {

    private final MenuItemRepository menuItemRepository;

    public MenuItemListService(MenuItemRepository menuItemRepository) {
        this.menuItemRepository = menuItemRepository;
    }

    public MenuItemListResponse listMenuItems() {
        List<MenuItemEntity> entities = menuItemRepository.findAllByOrderByDisplayOrderAscLabelAsc();
        
        // Retourner uniquement les menus racine (sans parent)
        // Les enfants seront inclus via la propriété children
        List<MenuItemEntity> rootItems = entities.stream()
            .filter(item -> item.getParent() == null)
            .toList();
        
        return new MenuItemListResponse(
            rootItems.stream().map(MenuItemModelMapper::toMenuItemModel).toList(),
            rootItems.size());
    }
}
