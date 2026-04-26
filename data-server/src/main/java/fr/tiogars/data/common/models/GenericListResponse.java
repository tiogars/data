package fr.tiogars.data.common.models;

import java.util.List;

public class GenericListResponse<T> {
    private List<T> items;
    private int count;

    public GenericListResponse(List<T> items) {
        this.items = items;
        this.count = items != null ? items.size() : 0;
    }

    public List<T> getItems() {
        return items;
    }

    public void setItems(List<T> items) {
        this.items = items;
        this.count = items != null ? items.size() : 0;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}