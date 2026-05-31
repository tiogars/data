package fr.tiogars.data.dev.githubrepository.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.dev.githubrepository.services.GitHubRepositoryDeleteOneService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "github-repository", description = "Opérations liées à la gestion des repositories GitHub.")
public class GitHubRepositoryDeleteOneController {

    private final GitHubRepositoryDeleteOneService gitHubRepositoryDeleteOneService;

    public GitHubRepositoryDeleteOneController(GitHubRepositoryDeleteOneService gitHubRepositoryDeleteOneService) {
        this.gitHubRepositoryDeleteOneService = gitHubRepositoryDeleteOneService;
    }

    @DeleteMapping("/github-repository/{id}")
    @Operation(summary = "Supprimer un repository GitHub", description = "Cette opération permet de supprimer un repository GitHub à partir de son identifiant.")
    public ResponseEntity<Void> deleteGitHubRepositoryById(@PathVariable String id) {
        gitHubRepositoryDeleteOneService.deleteGitHubRepositoryById(id);
        return ResponseEntity.noContent().build();
    }
}
