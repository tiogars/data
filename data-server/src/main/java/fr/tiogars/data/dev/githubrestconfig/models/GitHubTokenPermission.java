package fr.tiogars.data.dev.githubrestconfig.models;

import io.swagger.v3.oas.annotations.media.Schema;

public class GitHubTokenPermission {

    @Schema(description = "Permission fine-grained GitHub à configurer.", example = "Contents")
    private String permission;

    @Schema(description = "Niveau d'accès minimal requis.", example = "read")
    private String access;

    @Schema(description = "Pourquoi cette permission est nécessaire.", example = "Nécessaire pour lire les fichiers d'un repository.")
    private String reason;

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public String getAccess() {
        return access;
    }

    public void setAccess(String access) {
        this.access = access;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
