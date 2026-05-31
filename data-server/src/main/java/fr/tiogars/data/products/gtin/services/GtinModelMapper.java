package fr.tiogars.data.products.gtin.services;

import fr.tiogars.data.products.gtin.entities.GtinEntity;
import fr.tiogars.data.products.gtin.models.Gtin;

final class GtinModelMapper {

    private GtinModelMapper() {
    }

    static Gtin toModel(GtinEntity entity) {
        Gtin model = new Gtin();
        model.setId(entity.getId());
        model.setCode(entity.getCode());
        model.setDescription(entity.getDescription());
        return model;
    }
}
