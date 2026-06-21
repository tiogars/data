package fr.tiogars.data.settings.useraccount.services;

import org.springframework.stereotype.Service;

import fr.tiogars.data.common.exceptions.DataNotFoundException;
import fr.tiogars.data.settings.useraccount.models.UserAccount;
import fr.tiogars.data.settings.useraccount.repositories.UserAccountRepository;

@Service
public class UserAccountGetOneService {

    private final UserAccountRepository userAccountRepository;

    public UserAccountGetOneService(UserAccountRepository userAccountRepository) {
        this.userAccountRepository = userAccountRepository;
    }

    public UserAccount getUserAccount(String id) {
        return userAccountRepository.findById(id)
            .map(UserAccountModelMapper::toModel)
            .orElseThrow(() -> new DataNotFoundException("Compte utilisateur non trouve pour l'id: " + id));
    }
}
