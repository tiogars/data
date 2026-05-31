package fr.tiogars.data.dev.githubrestconfig.models;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

public class GitHubTokenPermissionResponse {

    @Schema(description = "Liste normalisée des opérations reconnues.")
    private List<String> operations;

    @Schema(description = "Liste des opérations non reconnues.")
    private List<String> unknownOperations;

    @Schema(description = "Liste agrégée des permissions GitHub minimales requises.")
    private List<GitHubTokenPermission> requiredPermissions;

    public GitHubTokenPermissionResponse(List<String> operations, List<String> unknownOperations, List<GitHubTokenPermission> requiredPermissions) {
        this.operations = operations;
        this.unknownOperations = unknownOperations;
        this.requiredPermissions = requiredPermissions;
    }

    public List<String> getOperations() {
        return operations;
    }

    public void setOperations(List<String> operations) {
        this.operations = operations;
    }

    public List<String> getUnknownOperations() {
        return unknownOperations;
    }

    public void setUnknownOperations(List<String> unknownOperations) {
        this.unknownOperations = unknownOperations;
    }

    public List<GitHubTokenPermission> getRequiredPermissions() {
        return requiredPermissions;
    }

    public void setRequiredPermissions(List<GitHubTokenPermission> requiredPermissions) {
        this.requiredPermissions = requiredPermissions;
    }
}
