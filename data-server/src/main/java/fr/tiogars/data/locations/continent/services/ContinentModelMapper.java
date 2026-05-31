package fr.tiogars.data.locations.continent.services;

import fr.tiogars.data.locations.continent.entities.ContinentEntity;
import fr.tiogars.data.locations.continent.models.Continent;

final class ContinentModelMapper {

    private ContinentModelMapper() {
    }

    static Continent toModel(ContinentEntity entity) {
        Continent model = new Continent();
        model.setId(entity.getId());
        model.setCode(entity.getCode());
        model.setName(entity.getName());
        return model;
    }
}
