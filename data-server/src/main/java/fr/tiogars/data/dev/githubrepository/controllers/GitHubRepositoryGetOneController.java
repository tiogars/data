package fr.tiogars.data.dev.githubrepository.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.dev.githubrepository.models.GitHubRepository;
import fr.tiogars.data.dev.githubrepository.services.GitHubRepositoryGetOneService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "github-repository", description = "Opérations liées à la gestion des repositories GitHub.")
public class GitHubRepositoryGetOneController {

    private final GitHubRepositoryGetOneService gitHubRepositoryGetOneService;

    public GitHubRepositoryGetOneController(GitHubRepositoryGetOneService gitHubRepositoryGetOneService) {
        this.gitHubRepositoryGetOneService = gitHubRepositoryGetOneService;
    }

    @GetMapping("/github-repository/{id}")
    @Operation(summary = "Récupérer un repository GitHub", description = "Cette opération permet de récupérer un repository GitHub à partir de son identifiant.")
    public ResponseEntity<GitHubRepository> getGitHubRepositoryById(@PathVariable String id) {
        return ResponseEntity.ok(gitHubRepositoryGetOneService.getGitHubRepositoryById(id));
    }
}
