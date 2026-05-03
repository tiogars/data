package fr.tiogars.data.dev.docs.menuitem.services;

import org.springframework.stereotype.Service;

import fr.tiogars.data.common.exceptions.DataNotFoundException;
import fr.tiogars.data.dev.docs.menuitem.models.MenuItem;
import fr.tiogars.data.dev.docs.menuitem.repositories.MenuItemRepository;

@Service
public class MenuItemGetOneService {

    private final MenuItemRepository menuItemRepository;

    public MenuItemGetOneService(MenuItemRepository menuItemRepository) {
        this.menuItemRepository = menuItemRepository;
    }

    public MenuItem getMenuItemById(String id) {
        return menuItemRepository.findById(id)
            .map(MenuItemModelMapper::toMenuItemModel)
            .orElseThrow(() -> new DataNotFoundException("Entree de menu non trouvee pour l'id: " + id));
    }
}
