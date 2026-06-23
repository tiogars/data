package fr.tiogars.data.cave.couleur.services;

import fr.tiogars.data.cave.couleur.entities.CouleurEntity;
import fr.tiogars.data.cave.couleur.models.Couleur;

final class CouleurModelMapper {
    private CouleurModelMapper() { }
    static Couleur toModel(CouleurEntity entity) {
        Couleur model = new Couleur();
        model.setId(entity.getId());
        model.setName(entity.getName());
        return model;
    }
}
