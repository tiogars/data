package fr.tiogars.data.settings.auth.models;

import io.swagger.v3.oas.annotations.media.Schema;

public class AuthLoginResponse {

    @Schema(description = "Le jeton JWT signe a transmettre dans l'en-tete Authorization.", example = "eyJhbGciOiJSUzI1NiJ9...")
    private String accessToken;

    @Schema(description = "Le type de jeton.", example = "Bearer")
    private String tokenType;

    @Schema(description = "La duree de validite du jeton, en secondes.", example = "28800")
    private long expiresIn;

    @Schema(description = "Le nom de connexion de l'utilisateur authentifie.", example = "admin")
    private String username;

    @Schema(description = "Le role de l'utilisateur authentifie (ADMIN ou USER).", example = "ADMIN")
    private String role;

    public AuthLoginResponse() {
    }

    public AuthLoginResponse(String accessToken, String tokenType, long expiresIn, String username, String role) {
        this.accessToken = accessToken;
        this.tokenType = tokenType;
        this.expiresIn = expiresIn;
        this.username = username;
        this.role = role;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public long getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(long expiresIn) {
        this.expiresIn = expiresIn;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
