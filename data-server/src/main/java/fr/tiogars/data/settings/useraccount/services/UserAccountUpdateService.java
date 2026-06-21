package fr.tiogars.data.settings.useraccount.services;

import org.springframework.stereotype.Service;

import fr.tiogars.data.common.exceptions.DataNotFoundException;
import fr.tiogars.data.settings.useraccount.entities.UserAccountEntity;
import fr.tiogars.data.settings.useraccount.forms.UserAccountUpdateForm;
import fr.tiogars.data.settings.useraccount.models.UserAccount;
import fr.tiogars.data.settings.useraccount.repositories.UserAccountRepository;

@Service
public class UserAccountUpdateService {

    private final UserAccountRepository userAccountRepository;
    private final UserAccountCreationService userAccountCreationService;
    private final UserAccountPasswordHashingService userAccountPasswordHashingService;

    public UserAccountUpdateService(
        UserAccountRepository userAccountRepository,
        UserAccountCreationService userAccountCreationService,
        UserAccountPasswordHashingService userAccountPasswordHashingService
    ) {
        this.userAccountRepository = userAccountRepository;
        this.userAccountCreationService = userAccountCreationService;
        this.userAccountPasswordHashingService = userAccountPasswordHashingService;
    }

    public UserAccount updateUserAccount(String id, UserAccountUpdateForm form) {
        UserAccountEntity entity = userAccountRepository.findById(id)
            .orElseThrow(() -> new DataNotFoundException("Compte utilisateur non trouve pour l'id: " + id));

        userAccountCreationService.validateUniqueUsername(form.getUsername(), id);
        UserAccountCreationService.applyValues(
            entity,
            form.getUsername(),
            form.getPassword(),
            form.getRole(),
            form.getEnabled(),
            userAccountPasswordHashingService,
            true
        );

        return UserAccountModelMapper.toModel(userAccountRepository.save(entity));
    }
}
