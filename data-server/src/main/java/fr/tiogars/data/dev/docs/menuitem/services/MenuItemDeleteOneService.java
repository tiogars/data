package fr.tiogars.data.dev.docs.menuitem.services;

import org.springframework.stereotype.Service;

import fr.tiogars.data.common.exceptions.DataNotFoundException;
import fr.tiogars.data.dev.docs.menuitem.repositories.MenuItemRepository;

@Service
public class MenuItemDeleteOneService {

    private final MenuItemRepository menuItemRepository;

    public MenuItemDeleteOneService(MenuItemRepository menuItemRepository) {
        this.menuItemRepository = menuItemRepository;
    }

    public void deleteMenuItemById(String id) {
        if (!menuItemRepository.existsById(id)) {
            throw new DataNotFoundException("Entree de menu non trouvee pour l'id: " + id);
        }

        menuItemRepository.deleteById(id);
    }
}
