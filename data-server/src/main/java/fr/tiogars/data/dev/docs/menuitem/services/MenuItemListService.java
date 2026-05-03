package fr.tiogars.data.dev.docs.menuitem.services;

import java.util.List;

import org.springframework.stereotype.Service;

import fr.tiogars.data.dev.docs.menuitem.entities.MenuItemEntity;
import fr.tiogars.data.dev.docs.menuitem.models.MenuItemListResponse;
import fr.tiogars.data.dev.docs.menuitem.repositories.MenuItemRepository;

@Service
public class MenuItemListService {

    private final MenuItemRepository menuItemRepository;

    public MenuItemListService(MenuItemRepository menuItemRepository) {
        this.menuItemRepository = menuItemRepository;
    }

    public MenuItemListResponse listMenuItems() {
        List<MenuItemEntity> entities = menuItemRepository.findAllByOrderByDisplayOrderAscLabelAsc();
        return new MenuItemListResponse(
            entities.stream().map(MenuItemModelMapper::toMenuItemModel).toList(),
            entities.size());
    }
}
