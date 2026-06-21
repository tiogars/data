package fr.tiogars.data.settings.useraccount.services;

import fr.tiogars.data.settings.useraccount.entities.UserAccountEntity;
import fr.tiogars.data.settings.useraccount.models.UserAccount;

final class UserAccountModelMapper {

    private UserAccountModelMapper() {
    }

    static UserAccount toModel(UserAccountEntity entity) {
        UserAccount model = new UserAccount();
        model.setId(entity.getId());
        model.setUsername(entity.getUsername());
        model.setRole(entity.getRole());
        model.setEnabled(entity.isEnabled());
        return model;
    }
}
