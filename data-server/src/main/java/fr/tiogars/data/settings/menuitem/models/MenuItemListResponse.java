package fr.tiogars.data.settings.menuitem.models;

import java.util.List;

import fr.tiogars.data.common.models.GenericListResponse;

public class MenuItemListResponse extends GenericListResponse<MenuItem> {
    public MenuItemListResponse(List<MenuItem> items) {
        super(items);
    }

    public MenuItemListResponse(List<MenuItem> items, int count) {
        super(items);
        setCount(count);
    }
}
