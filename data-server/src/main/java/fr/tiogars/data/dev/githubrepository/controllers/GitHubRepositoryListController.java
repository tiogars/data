package fr.tiogars.data.dev.githubrepository.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.dev.githubrepository.models.GitHubRepositoryListResponse;
import fr.tiogars.data.dev.githubrepository.services.GitHubRepositoryListService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "github-repository", description = "Opérations liées à la gestion des repositories GitHub.")
public class GitHubRepositoryListController {

    private final GitHubRepositoryListService gitHubRepositoryListService;

    public GitHubRepositoryListController(GitHubRepositoryListService gitHubRepositoryListService) {
        this.gitHubRepositoryListService = gitHubRepositoryListService;
    }

    @GetMapping("/github-repository")
    @Operation(summary = "Lister les repositories GitHub", description = "Cette opération permet de récupérer une liste paginée de repositories GitHub, avec recherche textuelle.")
    public ResponseEntity<GitHubRepositoryListResponse> listGitHubRepositories(
        @Parameter(description = "Index de page (commence à 0).", example = "0")
        @RequestParam(defaultValue = "0") int page,
        @Parameter(description = "Nombre d'éléments par page.", example = "10")
        @RequestParam(defaultValue = "10") int size,
        @Parameter(description = "Texte libre de recherche (owner, name, fullName, URL, description, branche, langage).", example = "spring")
        @RequestParam(required = false) String q
    ) {
        if (page < 0) {
            throw new IllegalArgumentException("Le paramètre page doit être supérieur ou égal à 0.");
        }

        if (size <= 0) {
            throw new IllegalArgumentException("Le paramètre size doit être supérieur à 0.");
        }

        if (size > 100) {
            throw new IllegalArgumentException("Le paramètre size ne peut pas dépasser 100.");
        }

        return ResponseEntity.ok(gitHubRepositoryListService.listGitHubRepositories(page, size, q));
    }
}
