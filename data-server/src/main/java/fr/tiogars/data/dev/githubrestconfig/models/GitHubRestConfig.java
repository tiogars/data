package fr.tiogars.data.dev.githubrestconfig.models;

import io.swagger.v3.oas.annotations.media.Schema;

public class GitHubRestConfig {

    @Schema(description = "Identifiant technique unique.", example = "123e4567-e89b-12d3-a456-426614174000")
    private String id;

    @Schema(description = "Identifiant fonctionnel du paramétrage.", example = "integration-ci")
    private String identifier;

    @Schema(description = "Token masqué pour éviter l'exposition en clair.", example = "ghp_****9X2A")
    private String tokenPreview;

    @Schema(description = "Commentaire de contexte.", example = "Token utilisé pour créer des issues depuis l'application")
    private String comment;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getTokenPreview() {
        return tokenPreview;
    }

    public void setTokenPreview(String tokenPreview) {
        this.tokenPreview = tokenPreview;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
