package fr.tiogars.data.dev.docs.menuitem.services;

import org.springframework.stereotype.Service;

import fr.tiogars.data.common.exceptions.DataNotFoundException;
import fr.tiogars.data.dev.docs.menuitem.entities.MenuItemEntity;
import fr.tiogars.data.dev.docs.menuitem.models.MenuItem;
import fr.tiogars.data.dev.docs.menuitem.repositories.MenuItemRepository;

@Service
public class MenuItemUpdateService {

    private final MenuItemRepository menuItemRepository;
    private final MenuItemCreationService menuItemCreationService;

    public MenuItemUpdateService(
        MenuItemRepository menuItemRepository,
        MenuItemCreationService menuItemCreationService
    ) {
        this.menuItemRepository = menuItemRepository;
        this.menuItemCreationService = menuItemCreationService;
    }

    public MenuItem updateMenuItem(String id, MenuItem menuItemUpdate) {
        MenuItemEntity entity = menuItemRepository.findById(id)
            .orElseThrow(() -> new DataNotFoundException("Entree de menu non trouvee pour l'id: " + id));

        menuItemCreationService.validateUniqueLabel(menuItemUpdate.getLabel(), id);
        menuItemCreationService.validateUniquePath(menuItemUpdate.getPath(), id);

        MenuItemCreationService.applyValues(
            entity,
            menuItemUpdate.getLabel(),
            menuItemUpdate.getPath(),
            menuItemUpdate.getIcon(),
            menuItemUpdate.getDisplayOrder());

        return MenuItemModelMapper.toMenuItemModel(menuItemRepository.save(entity));
    }
}
