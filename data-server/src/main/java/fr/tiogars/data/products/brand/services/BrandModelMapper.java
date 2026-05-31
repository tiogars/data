package fr.tiogars.data.products.brand.services;

import fr.tiogars.data.products.brand.entities.BrandEntity;
import fr.tiogars.data.products.brand.models.Brand;

final class BrandModelMapper {

    private BrandModelMapper() {
    }

    static Brand toModel(BrandEntity entity) {
        Brand model = new Brand();
        model.setId(entity.getId());
        model.setName(entity.getName());
        model.setDescription(entity.getDescription());
        return model;
    }
}
