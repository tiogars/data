package fr.tiogars.data.products.gtin.models;

import java.util.List;

import fr.tiogars.data.common.models.GenericListResponse;

public class GtinListResponse extends GenericListResponse<Gtin> {
    public GtinListResponse(List<Gtin> items) {
        super(items);
    }

    public GtinListResponse(List<Gtin> items, int count) {
        super(items);
        setCount(count);
    }
}
