package fr.tiogars.data.dev.model.models;

import java.util.List;

import fr.tiogars.data.common.models.GenericListResponse;

public class ModelSearchResponse extends GenericListResponse<Model> {

    private int page;
    private int size;
    private String query;

    public ModelSearchResponse(List<Model> items, int count, int page, int size, String query) {
        super(items);
        setCount(count);
        this.page = page;
        this.size = size;
        this.query = query;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }
}
