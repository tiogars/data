package fr.tiogars.data.settings.menuitem.services;

import org.springframework.stereotype.Service;

import fr.tiogars.data.settings.menuitem.repositories.MenuItemRepository;

@Service
public class MenuItemDeleteAllService {

    private final MenuItemRepository menuItemRepository;

    public MenuItemDeleteAllService(MenuItemRepository menuItemRepository) {
        this.menuItemRepository = menuItemRepository;
    }

    public void deleteAllMenuItems() {
        menuItemRepository.deleteAll();
    }
}
