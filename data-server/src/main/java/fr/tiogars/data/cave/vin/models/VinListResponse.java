package fr.tiogars.data.cave.vin.models;

import java.util.List;

import fr.tiogars.data.common.models.GenericListResponse;

public class VinListResponse extends GenericListResponse<Vin> {

    public VinListResponse(List<Vin> items) {
        super(items);
    }

    public VinListResponse(List<Vin> items, int count) {
        super(items);
        setCount(count);
    }
}
