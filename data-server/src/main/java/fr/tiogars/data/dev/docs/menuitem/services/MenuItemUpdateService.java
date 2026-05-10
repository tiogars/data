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

        // Mettre à jour le parent si un parentId est fourni
        if (menuItemUpdate.getParentId() != null && !menuItemUpdate.getParentId().isBlank()) {
            // Vérifier que le parent n'est pas l'entité elle-même
            if (menuItemUpdate.getParentId().equals(id)) {
                throw new IllegalArgumentException("Un menu ne peut pas être son propre parent.");
            }
            MenuItemEntity parent = menuItemRepository.findById(menuItemUpdate.getParentId())
                .orElseThrow(() -> new IllegalArgumentException("Le menu parent avec l'id " + menuItemUpdate.getParentId() + " n'existe pas."));
            entity.setParent(parent);
        } else {
            entity.setParent(null);
        }

        return MenuItemModelMapper.toMenuItemModel(menuItemRepository.save(entity));
    }
}
