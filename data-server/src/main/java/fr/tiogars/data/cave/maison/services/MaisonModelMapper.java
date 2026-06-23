package fr.tiogars.data.cave.maison.services;

import fr.tiogars.data.cave.maison.entities.MaisonEntity;
import fr.tiogars.data.cave.maison.models.Maison;

final class MaisonModelMapper { private MaisonModelMapper() { } static Maison toModel(MaisonEntity entity) { Maison model = new Maison(); model.setId(entity.getId()); model.setName(entity.getName()); model.setWebsite(entity.getWebsite()); return model; } }
