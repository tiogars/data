package fr.tiogars.data.settings.menuitem.services;

import java.util.stream.Collectors;

import fr.tiogars.data.settings.menuitem.entities.MenuItemEntity;
import fr.tiogars.data.settings.menuitem.models.MenuItem;

final class MenuItemModelMapper {

    private MenuItemModelMapper() {
    }

    static MenuItem toMenuItemModel(MenuItemEntity entity) {
        MenuItem model = new MenuItem();
        model.setId(entity.getId());
        model.setLabel(entity.getLabel());
        model.setPath(entity.getPath());
        model.setIcon(entity.getIcon());
        model.setDisplayOrder(entity.getDisplayOrder());
        model.setDefaultLoaded(entity.getDefaultLoaded());
        
        if (entity.getParent() != null) {
            model.setParentId(entity.getParent().getId());
        }
        
        if (entity.getChildren() != null && !entity.getChildren().isEmpty()) {
            model.setChildren(
                entity.getChildren().stream()
                    .map(MenuItemModelMapper::toMenuItemModel)
                    .collect(Collectors.toList())
            );
        }
        
        return model;
    }
}
