package fr.tiogars.data.dev.docs.githubrepository.models;

import io.swagger.v3.oas.annotations.media.Schema;

public class GitHubRepositorySyncResult {

    public enum SyncStatus {
        CREATED,
        UPDATED,
        MARKED_AS_MISSING
    }

    @Schema(description = "Résultat de la synchronisation.")
    private SyncStatus status;

    @Schema(description = "Repository local après synchronisation.")
    private GitHubRepository repository;

    @Schema(description = "Configuration token GitHub REST utilisée.", example = "integration-ci")
    private String usedConfigIdentifier;

    @Schema(description = "Message de synthèse du traitement.")
    private String message;

    public GitHubRepositorySyncResult() {
    }

    public GitHubRepositorySyncResult(SyncStatus status, GitHubRepository repository, String usedConfigIdentifier, String message) {
        this.status = status;
        this.repository = repository;
        this.usedConfigIdentifier = usedConfigIdentifier;
        this.message = message;
    }

    public SyncStatus getStatus() {
        return status;
    }

    public void setStatus(SyncStatus status) {
        this.status = status;
    }

    public GitHubRepository getRepository() {
        return repository;
    }

    public void setRepository(GitHubRepository repository) {
        this.repository = repository;
    }

    public String getUsedConfigIdentifier() {
        return usedConfigIdentifier;
    }

    public void setUsedConfigIdentifier(String usedConfigIdentifier) {
        this.usedConfigIdentifier = usedConfigIdentifier;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
