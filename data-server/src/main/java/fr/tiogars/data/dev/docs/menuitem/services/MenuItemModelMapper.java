package fr.tiogars.data.dev.docs.menuitem.services;

import fr.tiogars.data.dev.docs.menuitem.entities.MenuItemEntity;
import fr.tiogars.data.dev.docs.menuitem.models.MenuItem;
import java.util.stream.Collectors;

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
