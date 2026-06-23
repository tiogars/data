package fr.tiogars.data.cave.circonstance.services;

import fr.tiogars.data.cave.circonstance.entities.CirconstanceEntity;
import fr.tiogars.data.cave.circonstance.models.Circonstance;

final class CirconstanceModelMapper {
    private CirconstanceModelMapper() { }
    static Circonstance toModel(CirconstanceEntity entity) {
        Circonstance model = new Circonstance();
        model.setId(entity.getId());
        model.setName(entity.getName());
        return model;
    }
}
