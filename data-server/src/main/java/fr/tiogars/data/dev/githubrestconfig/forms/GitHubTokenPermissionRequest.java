package fr.tiogars.data.dev.githubrestconfig.forms;

import java.util.List;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;

public class GitHubTokenPermissionRequest {

    @ArraySchema(
        schema = @Schema(
            description = "Code opérationnel GitHub à exécuter (ex: repository.read, repository.write, issues.read, issues.write).",
            example = "repository.read"
        )
    )
    private List<String> operations;

    public List<String> getOperations() {
        return operations;
    }

    public void setOperations(List<String> operations) {
        this.operations = operations;
    }
}
