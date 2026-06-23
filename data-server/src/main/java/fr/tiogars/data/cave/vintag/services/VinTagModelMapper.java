package fr.tiogars.data.cave.vintag.services;

import fr.tiogars.data.cave.vintag.entities.VinTagEntity;
import fr.tiogars.data.cave.vintag.models.VinTag;

final class VinTagModelMapper {
    private VinTagModelMapper() { }
    static VinTag toModel(VinTagEntity entity) {
        VinTag model = new VinTag();
        model.setId(entity.getId());
        model.setName(entity.getName());
        return model;
    }
}
