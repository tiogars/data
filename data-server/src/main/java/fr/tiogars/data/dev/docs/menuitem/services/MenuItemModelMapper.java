package fr.tiogars.data.dev.docs.menuitem.services;

import fr.tiogars.data.dev.docs.menuitem.entities.MenuItemEntity;
import fr.tiogars.data.dev.docs.menuitem.models.MenuItem;

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
        return model;
    }
}
