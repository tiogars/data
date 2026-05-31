package fr.tiogars.data.dev.model.services;

import fr.tiogars.data.dev.model.entities.ModelAttributeEntity;
import fr.tiogars.data.dev.model.entities.ModelEntity;
import fr.tiogars.data.dev.model.models.Model;
import fr.tiogars.data.dev.model.models.ModelAttribute;

final class ModelMapper {

    private ModelMapper() {
    }

    static Model toModel(ModelEntity entity) {
        Model model = new Model();
        model.setId(entity.getId());
        model.setName(entity.getName());
        model.setDescription(entity.getDescription());
        model.setModelAttributes(entity.getModelAttributes().stream().map(ModelMapper::toModelAttribute).toList());
        return model;
    }

    static ModelAttribute toModelAttribute(ModelAttributeEntity entity) {
        ModelAttribute attribute = new ModelAttribute();
        attribute.setId(entity.getId());
        attribute.setName(entity.getName());
        attribute.setDescription(entity.getDescription());
        return attribute;
    }
}
