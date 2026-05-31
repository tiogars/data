package fr.tiogars.data.dev.model.models;

import java.util.List;

import fr.tiogars.data.common.models.GenericListResponse;

public class ModelListResponse extends GenericListResponse<Model> {
    public ModelListResponse(List<Model> items) {
        super(items);
    }

    public ModelListResponse(List<Model> items, int count) {
        super(items);
        setCount(count);
    }
}
