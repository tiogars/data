package fr.tiogars.data.cave.contenant.services;

import fr.tiogars.data.cave.contenant.entities.ContenantEntity;
import fr.tiogars.data.cave.contenant.models.Contenant;

final class ContenantModelMapper { private ContenantModelMapper() { } static Contenant toModel(ContenantEntity entity) { Contenant model = new Contenant(); model.setId(entity.getId()); model.setName(entity.getName()); model.setVolumeCl(entity.getVolumeCl()); return model; } }
