package fr.tiogars.data.cave.typevin.services;

import fr.tiogars.data.cave.typevin.entities.TypeVinEntity;
import fr.tiogars.data.cave.typevin.models.TypeVin;

final class TypeVinModelMapper {
    private TypeVinModelMapper() { }
    static TypeVin toModel(TypeVinEntity entity) {
        TypeVin model = new TypeVin();
        model.setId(entity.getId());
        model.setName(entity.getName());
        return model;
    }
}
