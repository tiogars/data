package fr.tiogars.data.dev.docs.githubrepository.forms;

import io.swagger.v3.oas.annotations.media.Schema;

public class GitHubRepositorySyncForm {

    @Schema(description = "Le propriétaire du repository à synchroniser.", example = "tiogars")
    private String owner;

    @Schema(description = "Le nom du repository à synchroniser.", example = "data")
    private String name;

    @Schema(description = "L'identifiant de la configuration GitHub REST à utiliser.", example = "integration-ci")
    private String gitHubRestConfigIdentifier;

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGitHubRestConfigIdentifier() {
        return gitHubRestConfigIdentifier;
    }

    public void setGitHubRestConfigIdentifier(String gitHubRestConfigIdentifier) {
        this.gitHubRestConfigIdentifier = gitHubRestConfigIdentifier;
    }
}
