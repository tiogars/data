package fr.tiogars.data.dev.docs.githubrepository.models;

import io.swagger.v3.oas.annotations.media.Schema;

public class GitHubRepositoryBulkSyncResult {

    @Schema(description = "Nombre de repositories créés localement depuis GitHub.")
    private int created;

    @Schema(description = "Nombre de repositories mis à jour depuis GitHub.")
    private int updated;

    @Schema(description = "Nombre de repositories marqués comme inexistants sur GitHub.")
    private int markedAsMissing;

    @Schema(description = "Nombre total de repositories traités.")
    private int total;

    @Schema(description = "Configuration token GitHub REST utilisée.", example = "integration-ci")
    private String usedConfigIdentifier;

    public GitHubRepositoryBulkSyncResult() {
    }

    public GitHubRepositoryBulkSyncResult(int created, int updated, int markedAsMissing, int total, String usedConfigIdentifier) {
        this.created = created;
        this.updated = updated;
        this.markedAsMissing = markedAsMissing;
        this.total = total;
        this.usedConfigIdentifier = usedConfigIdentifier;
    }

    public int getCreated() {
        return created;
    }

    public void setCreated(int created) {
        this.created = created;
    }

    public int getUpdated() {
        return updated;
    }

    public void setUpdated(int updated) {
        this.updated = updated;
    }

    public int getMarkedAsMissing() {
        return markedAsMissing;
    }

    public void setMarkedAsMissing(int markedAsMissing) {
        this.markedAsMissing = markedAsMissing;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getUsedConfigIdentifier() {
        return usedConfigIdentifier;
    }

    public void setUsedConfigIdentifier(String usedConfigIdentifier) {
        this.usedConfigIdentifier = usedConfigIdentifier;
    }
}
