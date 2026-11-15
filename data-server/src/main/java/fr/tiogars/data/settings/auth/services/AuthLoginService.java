package fr.tiogars.data.settings.auth.services;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import fr.tiogars.data.common.exceptions.InvalidCredentialsException;
import fr.tiogars.data.settings.auth.forms.AuthLoginForm;
import fr.tiogars.data.settings.auth.models.AuthLoginResponse;
import fr.tiogars.data.settings.auth.services.AuthTokenService.IssuedToken;
import fr.tiogars.data.settings.useraccount.entities.UserAccountEntity;
import fr.tiogars.data.settings.useraccount.repositories.UserAccountRepository;
import fr.tiogars.data.settings.useraccount.services.UserAccountPasswordHashingService;

/**
 * Authentifie un utilisateur a partir des comptes enregistres en base de donnees.
 */
@Service
public class AuthLoginService {

    static final String INVALID_CREDENTIALS_MESSAGE = "Nom utilisateur ou mot de passe incorrect.";

    private final UserAccountRepository userAccountRepository;
    private final UserAccountPasswordHashingService passwordHashingService;
    private final AuthTokenService authTokenService;
    private final String unknownAccountPasswordHash;

    public AuthLoginService(
        UserAccountRepository userAccountRepository,
        UserAccountPasswordHashingService passwordHashingService,
        AuthTokenService authTokenService
    ) {
        this.userAccountRepository = userAccountRepository;
        this.passwordHashingService = passwordHashingService;
        this.authTokenService = authTokenService;
        this.unknownAccountPasswordHash = passwordHashingService.hashPassword(UUID.randomUUID().toString());
    }

    /**
     * Verifie les identifiants et emet un jeton d'acces.
     *
     * @throws IllegalArgumentException si le nom utilisateur ou le mot de passe est vide
     * @throws InvalidCredentialsException si le compte est inconnu, desactive ou si le mot de passe est incorrect
     */
    public AuthLoginResponse login(AuthLoginForm form) {
        if (form == null || isBlank(form.getUsername()) || isBlank(form.getPassword())) {
            throw new IllegalArgumentException("Le nom utilisateur et le mot de passe sont obligatoires.");
        }

        Optional<UserAccountEntity> account = userAccountRepository
            .findByUsername(form.getUsername().trim())
            .filter(UserAccountEntity::isEnabled);

        // Le mot de passe est enregistre apres trim a la creation du compte.
        // Un hash factice est verifie pour un compte inconnu afin d'eviter de reveler son existence par le temps de reponse.
        String expectedHash = account.map(UserAccountEntity::getPasswordHash).orElse(unknownAccountPasswordHash);
        boolean passwordMatches = passwordHashingService.matches(form.getPassword().trim(), expectedHash);

        if (account.isEmpty() || !passwordMatches) {
            throw new InvalidCredentialsException(INVALID_CREDENTIALS_MESSAGE);
        }

        UserAccountEntity user = account.get();
        IssuedToken token = authTokenService.issueToken(user.getUsername(), user.getRole());
        return new AuthLoginResponse(token.value(), "Bearer", token.validity().toSeconds(), user.getUsername(), user.getRole());
    }

    private static boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}
