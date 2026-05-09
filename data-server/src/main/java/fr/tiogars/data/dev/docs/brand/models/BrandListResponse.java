package fr.tiogars.data.dev.docs.brand.models;

import java.util.List;

import fr.tiogars.data.common.models.GenericListResponse;

public class BrandListResponse extends GenericListResponse<Brand> {
    public BrandListResponse(List<Brand> items) {
        super(items);
    }

    public BrandListResponse(List<Brand> items, int count) {
        super(items);
        setCount(count);
    }
}
