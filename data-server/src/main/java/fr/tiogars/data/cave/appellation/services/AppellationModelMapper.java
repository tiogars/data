package fr.tiogars.data.cave.appellation.services;

import fr.tiogars.data.cave.appellation.entities.AppellationEntity;
import fr.tiogars.data.cave.appellation.models.Appellation;

final class AppellationModelMapper {
    private AppellationModelMapper() { }
    static Appellation toModel(AppellationEntity entity) {
        Appellation model = new Appellation();
        model.setId(entity.getId());
        model.setName(entity.getName());
        return model;
    }
}
