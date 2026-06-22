package fr.tiogars.data.softwares.winget.services;

import fr.tiogars.data.softwares.winget.entities.WingetEntity;
import fr.tiogars.data.softwares.winget.models.Winget;

public final class WingetModelMapper {

    private WingetModelMapper() {
    }

    public static Winget toModel(WingetEntity entity) {
        Winget model = new Winget();
        model.setId(entity.getId());
        model.setName(entity.getName());
        model.setDescription(entity.getDescription());
        model.setWingetId(entity.getWingetId());
        model.setInstallCommand(entity.getInstallCommand());
        model.setTags(entity.getTags());
        model.setUpdatedAt(entity.getUpdatedAt());
        return model;
    }
}
