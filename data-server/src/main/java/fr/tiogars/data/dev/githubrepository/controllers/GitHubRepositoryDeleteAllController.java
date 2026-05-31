package fr.tiogars.data.dev.githubrepository.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.dev.githubrepository.services.GitHubRepositoryDeleteAllService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "github-repository", description = "Opérations liées à la gestion des repositories GitHub.")
public class GitHubRepositoryDeleteAllController {

    private final GitHubRepositoryDeleteAllService gitHubRepositoryDeleteAllService;

    public GitHubRepositoryDeleteAllController(GitHubRepositoryDeleteAllService gitHubRepositoryDeleteAllService) {
        this.gitHubRepositoryDeleteAllService = gitHubRepositoryDeleteAllService;
    }

    @DeleteMapping("/github-repository")
    @Operation(summary = "Supprimer tous les repositories GitHub", description = "Cette opération permet de supprimer tous les repositories GitHub enregistrés.")
    public ResponseEntity<Void> deleteAllGitHubRepositories() {
        gitHubRepositoryDeleteAllService.deleteAllGitHubRepositories();
        return ResponseEntity.noContent().build();
    }
}
