package fr.tiogars.data.dev.githubrestconfig.forms;

import io.swagger.v3.oas.annotations.media.Schema;

public class GitHubRestConfigCreationForm {

    @Schema(description = "Identifiant fonctionnel pour retrouver ce paramétrage.", example = "integration-ci")
    private String identifier;

    @Schema(description = "Token d'accès GitHub REST.", example = "github_pat_xxx")
    private String token;

    @Schema(description = "Commentaire libre pour documenter l'usage du token.", example = "Token utilisé pour créer des issues depuis l'application")
    private String comment;

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
