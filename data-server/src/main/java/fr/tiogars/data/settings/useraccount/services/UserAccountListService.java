package fr.tiogars.data.settings.useraccount.services;

import java.util.List;

import org.springframework.stereotype.Service;

import fr.tiogars.data.settings.useraccount.entities.UserAccountEntity;
import fr.tiogars.data.settings.useraccount.models.UserAccountListResponse;
import fr.tiogars.data.settings.useraccount.repositories.UserAccountRepository;

@Service
public class UserAccountListService {

    private final UserAccountRepository userAccountRepository;

    public UserAccountListService(UserAccountRepository userAccountRepository) {
        this.userAccountRepository = userAccountRepository;
    }

    public UserAccountListResponse listUserAccounts() {
        List<UserAccountEntity> entities = userAccountRepository.findAllByOrderByUsernameAsc();
        return new UserAccountListResponse(entities.stream().map(UserAccountModelMapper::toModel).toList(), entities.size());
    }
}
