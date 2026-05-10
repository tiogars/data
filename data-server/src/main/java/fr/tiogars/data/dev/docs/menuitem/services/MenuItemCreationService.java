package fr.tiogars.data.dev.docs.menuitem.services;

import org.springframework.stereotype.Service;

import fr.tiogars.data.dev.docs.menuitem.entities.MenuItemEntity;
import fr.tiogars.data.dev.docs.menuitem.forms.MenuItemCreationForm;
import fr.tiogars.data.dev.docs.menuitem.models.MenuItem;
import fr.tiogars.data.dev.docs.menuitem.repositories.MenuItemRepository;

@Service
public class MenuItemCreationService {

    private final MenuItemRepository menuItemRepository;

    public MenuItemCreationService(MenuItemRepository menuItemRepository) {
        this.menuItemRepository = menuItemRepository;
    }

    public MenuItem createMenuItem(MenuItemCreationForm form) {
        validateUniqueLabel(form.getLabel(), null);
        validateUniquePath(form.getPath(), null);

        MenuItemEntity entity = new MenuItemEntity();
        applyValues(entity, form.getLabel(), form.getPath(), form.getIcon(), form.getDisplayOrder());
        entity.setDefaultLoaded(false);
        
        // Définir le parent si un parentId est fourni
        if (form.getParentId() != null && !form.getParentId().isBlank()) {
            MenuItemEntity parent = menuItemRepository.findById(form.getParentId())
                .orElseThrow(() -> new IllegalArgumentException("Le menu parent avec l'id " + form.getParentId() + " n'existe pas."));
            entity.setParent(parent);
        }

        return MenuItemModelMapper.toMenuItemModel(menuItemRepository.save(entity));
    }

    static void applyValues(MenuItemEntity entity, String label, String path, String icon, Integer displayOrder) {
        entity.setLabel(requireText(label, "Le libelle est obligatoire."));
        entity.setPath(requirePath(path));
        entity.setIcon(requireText(icon, "L'icone est obligatoire."));
        entity.setDisplayOrder(displayOrder != null ? displayOrder : 0);
    }

    void validateUniqueLabel(String label, String currentId) {
        menuItemRepository.findByLabel(requireText(label, "Le libelle est obligatoire."))
            .filter(entity -> !entity.getId().equals(currentId))
            .ifPresent(entity -> {
                throw new IllegalArgumentException("Une entree de menu avec ce libelle existe deja.");
            });
    }

    void validateUniquePath(String path, String currentId) {
        menuItemRepository.findByPath(requirePath(path))
            .filter(entity -> !entity.getId().equals(currentId))
            .ifPresent(entity -> {
                throw new IllegalArgumentException("Une entree de menu avec ce chemin existe deja.");
            });
    }

    static String requirePath(String value) {
        String trimmed = requireText(value, "Le chemin est obligatoire.");
        if (!trimmed.startsWith("/")) {
            throw new IllegalArgumentException("Le chemin doit commencer par '/'.");
        }
        return trimmed;
    }

    static String requireText(String value, String message) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(message);
        }
        return value.trim();
    }
}
