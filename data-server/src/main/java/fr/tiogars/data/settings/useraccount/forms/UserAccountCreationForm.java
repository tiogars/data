package fr.tiogars.data.settings.useraccount.forms;

import io.swagger.v3.oas.annotations.media.Schema;

public class UserAccountCreationForm {

    @Schema(description = "Le nom de connexion de l'utilisateur.", example = "admin")
    private String username;

    @Schema(description = "Le mot de passe en clair saisi pour le compte.", example = "admin123")
    private String password;

    @Schema(description = "Le role de l'utilisateur (ADMIN ou USER).", example = "ADMIN")
    private String role;

    @Schema(description = "Indique si le compte est actif.", example = "true")
    private Boolean enabled;

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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }
}
