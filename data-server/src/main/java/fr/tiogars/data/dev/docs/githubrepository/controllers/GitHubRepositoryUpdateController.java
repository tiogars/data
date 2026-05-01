package fr.tiogars.data.dev.docs.githubrepository.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.dev.docs.githubrepository.models.GitHubRepository;
import fr.tiogars.data.dev.docs.githubrepository.services.GitHubRepositoryUpdateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Tag(name = "github-repository", description = "Opérations liées à la gestion des repositories GitHub.")
public class GitHubRepositoryUpdateController {

    private final GitHubRepositoryUpdateService gitHubRepositoryUpdateService;

    public GitHubRepositoryUpdateController(GitHubRepositoryUpdateService gitHubRepositoryUpdateService) {
        this.gitHubRepositoryUpdateService = gitHubRepositoryUpdateService;
    }

    @PutMapping("/github-repository/{id}")
    @Operation(summary = "Modifier un repository GitHub", description = "Cette opération permet de modifier un repository GitHub existant.")
    public ResponseEntity<GitHubRepository> updateGitHubRepository(@PathVariable String id, @RequestBody GitHubRepository repository) {
        return ResponseEntity.ok(gitHubRepositoryUpdateService.updateGitHubRepository(id, repository));
    }
}
