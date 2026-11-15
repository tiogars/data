package fr.tiogars.data.settings.auth.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.Duration;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.nimbusds.jwt.SignedJWT;

import fr.tiogars.data.common.exceptions.InvalidCredentialsException;
import fr.tiogars.data.settings.auth.forms.AuthLoginForm;
import fr.tiogars.data.settings.auth.models.AuthLoginResponse;
import fr.tiogars.data.settings.useraccount.entities.UserAccountEntity;
import fr.tiogars.data.settings.useraccount.repositories.UserAccountRepository;
import fr.tiogars.data.settings.useraccount.services.UserAccountPasswordHashingService;

class AuthLoginServiceTest {

    private final UserAccountPasswordHashingService hasher = new UserAccountPasswordHashingService();
    private final AuthTokenService tokenService = new AuthTokenService("data-server", Duration.ofHours(1));
    private UserAccountRepository repository;
    private AuthLoginService service;

    @BeforeEach
    void setUp() {
        repository = mock(UserAccountRepository.class);
        service = new AuthLoginService(repository, hasher, tokenService);
    }

    @Test
    void shouldReturnSignedTokenWhenCredentialsAreValid() throws Exception {
        when(repository.findByUsername("tiogars")).thenReturn(Optional.of(account("tiogars", "motdepasse123", "ADMIN", true)));

        AuthLoginResponse response = service.login(form(" tiogars ", " motdepasse123 "));

        assertThat(response.getUsername()).isEqualTo("tiogars");
        assertThat(response.getRole()).isEqualTo("ADMIN");
        assertThat(response.getTokenType()).isEqualTo("Bearer");
        assertThat(response.getExpiresIn()).isEqualTo(3600);

        SignedJWT jwt = SignedJWT.parse(response.getAccessToken());
        assertThat(jwt.getJWTClaimsSet().getSubject()).isEqualTo("tiogars");
        assertThat(jwt.getJWTClaimsSet().getIssuer()).isEqualTo("data-server");
        assertThat(jwt.getJWTClaimsSet().getStringListClaim("roles")).containsExactly("ADMIN");
    }

    @Test
    void shouldThrowInvalidCredentialsWhenPasswordIsWrong() {
        when(repository.findByUsername("tiogars")).thenReturn(Optional.of(account("tiogars", "motdepasse123", "ADMIN", true)));

        assertThatThrownBy(() -> service.login(form("tiogars", "mauvais-mot-de-passe")))
            .isInstanceOf(InvalidCredentialsException.class)
            .hasMessage(AuthLoginService.INVALID_CREDENTIALS_MESSAGE);
    }

    @Test
    void shouldThrowInvalidCredentialsWhenAccountIsUnknown() {
        when(repository.findByUsername("inconnu")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.login(form("inconnu", "motdepasse123")))
            .isInstanceOf(InvalidCredentialsException.class)
            .hasMessage(AuthLoginService.INVALID_CREDENTIALS_MESSAGE);
    }

    @Test
    void shouldThrowInvalidCredentialsWhenAccountIsDisabled() {
        when(repository.findByUsername("tiogars")).thenReturn(Optional.of(account("tiogars", "motdepasse123", "USER", false)));

        assertThatThrownBy(() -> service.login(form("tiogars", "motdepasse123")))
            .isInstanceOf(InvalidCredentialsException.class);
    }

    @Test
    void shouldThrowIllegalArgumentWhenUsernameOrPasswordIsBlank() {
        assertThatThrownBy(() -> service.login(form("  ", "motdepasse123")))
            .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> service.login(form("tiogars", null)))
            .isInstanceOf(IllegalArgumentException.class);
    }

    private UserAccountEntity account(String username, String password, String role, boolean enabled) {
        UserAccountEntity entity = new UserAccountEntity();
        entity.setUsername(username);
        entity.setPasswordHash(hasher.hashPassword(password));
        entity.setRole(role);
        entity.setEnabled(enabled);
        return entity;
    }

    private AuthLoginForm form(String username, String password) {
        AuthLoginForm form = new AuthLoginForm();
        form.setUsername(username);
        form.setPassword(password);
        return form;
    }
}
