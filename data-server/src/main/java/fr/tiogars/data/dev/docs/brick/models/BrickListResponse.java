package fr.tiogars.data.dev.docs.brick.models;

import java.util.List;

import fr.tiogars.data.common.models.GenericListResponse;

public class BrickListResponse extends GenericListResponse<Brick> {
    public BrickListResponse(List<Brick> items) {
        super(items);
    }

    public BrickListResponse(List<Brick> items, int count) {
        super(items);
        setCount(count);
    }
}
