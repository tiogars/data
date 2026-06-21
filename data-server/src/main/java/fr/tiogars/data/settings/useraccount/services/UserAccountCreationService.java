package fr.tiogars.data.settings.useraccount.services;

import org.springframework.stereotype.Service;

import fr.tiogars.data.settings.useraccount.entities.UserAccountEntity;
import fr.tiogars.data.settings.useraccount.forms.UserAccountCreationForm;
import fr.tiogars.data.settings.useraccount.models.UserAccount;
import fr.tiogars.data.settings.useraccount.repositories.UserAccountRepository;

@Service
public class UserAccountCreationService {

    private final UserAccountRepository userAccountRepository;
    private final UserAccountPasswordHashingService userAccountPasswordHashingService;

    public UserAccountCreationService(
        UserAccountRepository userAccountRepository,
        UserAccountPasswordHashingService userAccountPasswordHashingService
    ) {
        this.userAccountRepository = userAccountRepository;
        this.userAccountPasswordHashingService = userAccountPasswordHashingService;
    }

    public UserAccount createUserAccount(UserAccountCreationForm form) {
        validateUniqueUsername(form.getUsername(), null);

        UserAccountEntity entity = new UserAccountEntity();
        applyValues(
            entity,
            form.getUsername(),
            form.getPassword(),
            form.getRole(),
            form.getEnabled(),
            userAccountPasswordHashingService,
            false
        );

        return UserAccountModelMapper.toModel(userAccountRepository.save(entity));
    }

    static void applyValues(
        UserAccountEntity entity,
        String username,
        String password,
        String role,
        Boolean enabled,
        UserAccountPasswordHashingService hasher,
        boolean keepExistingPasswordIfBlank
    ) {
        entity.setUsername(requireText(username, "Le nom utilisateur est obligatoire."));
        entity.setRole(requireRole(role));
        entity.setEnabled(enabled == null ? true : enabled);

        if (!keepExistingPasswordIfBlank || (password != null && !password.isBlank())) {
            entity.setPasswordHash(hasher.hashPassword(requirePassword(password)));
        }
    }

    void validateUniqueUsername(String username, String currentId) {
        userAccountRepository.findByUsername(requireText(username, "Le nom utilisateur est obligatoire."))
            .filter(entity -> !entity.getId().equals(currentId))
            .ifPresent(entity -> {
                throw new IllegalArgumentException("Un compte avec ce nom utilisateur existe deja.");
            });
    }

    static String requireText(String value, String message) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(message);
        }
        return value.trim();
    }

    static String requirePassword(String password) {
        String trimmed = requireText(password, "Le mot de passe est obligatoire.");
        if (trimmed.length() < 8) {
            throw new IllegalArgumentException("Le mot de passe doit contenir au moins 8 caracteres.");
        }
        return trimmed;
    }

    static String requireRole(String role) {
        String normalized = requireText(role, "Le role est obligatoire.").toUpperCase();
        if (!"ADMIN".equals(normalized) && !"USER".equals(normalized)) {
            throw new IllegalArgumentException("Le role doit etre ADMIN ou USER.");
        }
        return normalized;
    }
}
