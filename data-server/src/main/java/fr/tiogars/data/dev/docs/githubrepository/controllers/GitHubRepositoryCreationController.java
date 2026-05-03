package fr.tiogars.data.dev.docs.githubrepository.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.dev.docs.githubrepository.forms.GitHubRepositoryCreationForm;
import fr.tiogars.data.dev.docs.githubrepository.models.GitHubRepository;
import fr.tiogars.data.dev.docs.githubrepository.services.GitHubRepositoryCreationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "github-repository", description = "Opérations liées à la gestion des repositories GitHub.")
public class GitHubRepositoryCreationController {

    private final GitHubRepositoryCreationService gitHubRepositoryCreationService;

    public GitHubRepositoryCreationController(GitHubRepositoryCreationService gitHubRepositoryCreationService) {
        this.gitHubRepositoryCreationService = gitHubRepositoryCreationService;
    }

    @PostMapping("/github-repository")
    @Operation(summary = "Créer un repository GitHub", description = "Cette opération permet de créer un nouveau repository GitHub dans la base applicative.")
    public ResponseEntity<GitHubRepository> createGitHubRepository(@RequestBody GitHubRepositoryCreationForm form) {
        return ResponseEntity.ok(gitHubRepositoryCreationService.createGitHubRepository(form));
    }
}
