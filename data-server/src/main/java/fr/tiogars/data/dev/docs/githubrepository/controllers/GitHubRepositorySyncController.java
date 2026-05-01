package fr.tiogars.data.dev.docs.githubrepository.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.dev.docs.githubrepository.forms.GitHubRepositoryBulkSyncForm;
import fr.tiogars.data.dev.docs.githubrepository.forms.GitHubRepositorySyncForm;
import fr.tiogars.data.dev.docs.githubrepository.models.GitHubRepositoryBulkSyncResult;
import fr.tiogars.data.dev.docs.githubrepository.models.GitHubRepositorySyncResult;
import fr.tiogars.data.dev.docs.githubrepository.services.GitHubRepositorySyncService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Tag(name = "github-repository", description = "Opérations liées à la gestion des repositories GitHub.")
public class GitHubRepositorySyncController {

    private final GitHubRepositorySyncService gitHubRepositorySyncService;

    public GitHubRepositorySyncController(GitHubRepositorySyncService gitHubRepositorySyncService) {
        this.gitHubRepositorySyncService = gitHubRepositorySyncService;
    }

    @PostMapping("/github-repository/sync")
    @Operation(
        summary = "Synchroniser un repository GitHub via token REST",
        description = "Crée ou met à jour le repository en local depuis GitHub, ou le marque comme inexistant si GitHub répond 404.")
    public ResponseEntity<GitHubRepositorySyncResult> syncGitHubRepository(@RequestBody GitHubRepositorySyncForm form) {
        return ResponseEntity.ok(gitHubRepositorySyncService.syncRepository(
            form.getOwner(),
            form.getName(),
            form.getGitHubRestConfigIdentifier()));
    }

    @PostMapping("/github-repository/sync/all")
    @Operation(
        summary = "Synchroniser tous les repositories GitHub de l'utilisateur",
        description = "Récupère tous les repositories de l'utilisateur lié au token de la configuration, les crée ou met à jour localement, puis marque comme inexistants ceux absents de la réponse GitHub.")
    public ResponseEntity<GitHubRepositoryBulkSyncResult> syncAllGitHubRepositories(@RequestBody GitHubRepositoryBulkSyncForm form) {
        return ResponseEntity.ok(gitHubRepositorySyncService.syncAllRepositories(
            form.getGitHubRestConfigIdentifier()));
    }
}
