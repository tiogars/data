package fr.tiogars.data.settings.auth.forms;

import io.swagger.v3.oas.annotations.media.Schema;

public class AuthLoginForm {

    @Schema(description = "Le nom de connexion du compte utilisateur.", example = "admin")
    private String username;

    @Schema(description = "Le mot de passe en clair du compte utilisateur.", example = "motdepasse123")
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
