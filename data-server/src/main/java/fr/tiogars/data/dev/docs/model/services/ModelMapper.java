package fr.tiogars.data.dev.docs.model.services;

import fr.tiogars.data.dev.docs.model.entities.ModelEntity;
import fr.tiogars.data.dev.docs.model.models.Model;

final class ModelMapper {

    private ModelMapper() {
    }

    static Model toModel(ModelEntity entity) {
        Model model = new Model();
        model.setId(entity.getId());
        model.setName(entity.getName());
        model.setDescription(entity.getDescription());
        return model;
    }
}
