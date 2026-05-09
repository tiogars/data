package fr.tiogars.data.dev.docs.brand.services;

import fr.tiogars.data.dev.docs.brand.entities.BrandEntity;
import fr.tiogars.data.dev.docs.brand.models.Brand;

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
