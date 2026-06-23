package fr.tiogars.data.cave.cepage.services;

import fr.tiogars.data.cave.cepage.entities.CepageEntity;
import fr.tiogars.data.cave.cepage.models.Cepage;

final class CepageModelMapper {
    private CepageModelMapper() { }
    static Cepage toModel(CepageEntity entity) {
        Cepage model = new Cepage();
        model.setId(entity.getId());
        model.setName(entity.getName());
        return model;
    }
}
