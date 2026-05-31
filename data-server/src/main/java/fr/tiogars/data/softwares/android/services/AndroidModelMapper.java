package fr.tiogars.data.softwares.android.services;

import java.util.ArrayList;

import fr.tiogars.data.softwares.android.entities.AndroidEntity;
import fr.tiogars.data.softwares.android.models.Android;

public final class AndroidModelMapper {

    private AndroidModelMapper() {
    }

    public static Android toModel(AndroidEntity entity) {
        if (entity == null) {
            return null;
        }

        Android model = new Android();
        model.setId(entity.getId());
        model.setName(entity.getName());
        model.setPackageName(entity.getPackageName());
        model.setCategory(entity.getCategory() != null ? new ArrayList<>(entity.getCategory()) : new ArrayList<>());
        model.setDescription(entity.getDescription());
        model.setIcon(entity.getIcon());
        return model;
    }
}