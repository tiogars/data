package fr.tiogars.data.settings.useraccount.forms;

import io.swagger.v3.oas.annotations.media.Schema;

public class UserAccountUpdateForm {

    @Schema(description = "L'identifiant unique du compte utilisateur.", example = "123e4567-e89b-12d3-a456-426614174000")
    private String id;

    @Schema(description = "Le nom de connexion de l'utilisateur.", example = "admin")
    private String username;

    @Schema(description = "Nouveau mot de passe en clair. Si vide, le mot de passe actuel est conserve.", example = "nouveauMotDePasse")
    private String password;

    @Schema(description = "Le role de l'utilisateur (ADMIN ou USER).", example = "ADMIN")
    private String role;

    @Schema(description = "Indique si le compte est actif.", example = "true")
    private Boolean enabled;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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
