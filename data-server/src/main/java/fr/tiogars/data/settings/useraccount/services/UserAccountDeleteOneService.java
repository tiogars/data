package fr.tiogars.data.settings.useraccount.services;

import org.springframework.stereotype.Service;

import fr.tiogars.data.common.exceptions.DataNotFoundException;
import fr.tiogars.data.settings.useraccount.repositories.UserAccountRepository;

@Service
public class UserAccountDeleteOneService {

    private final UserAccountRepository userAccountRepository;

    public UserAccountDeleteOneService(UserAccountRepository userAccountRepository) {
        this.userAccountRepository = userAccountRepository;
    }

    public void deleteUserAccount(String id) {
        if (!userAccountRepository.existsById(id)) {
            throw new DataNotFoundException("Compte utilisateur non trouve pour l'id: " + id);
        }
        userAccountRepository.deleteById(id);
    }
}
