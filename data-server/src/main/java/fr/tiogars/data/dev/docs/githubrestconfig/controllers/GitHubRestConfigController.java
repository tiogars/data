package fr.tiogars.data.dev.docs.githubrestconfig.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.dev.docs.githubrestconfig.forms.GitHubRestConfigCreationForm;
import fr.tiogars.data.dev.docs.githubrestconfig.models.GitHubRestConfig;
import fr.tiogars.data.dev.docs.githubrestconfig.services.GitHubRestConfigCreationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Tag(name = "github-rest-config", description = "Paramétrage de la connexion REST vers GitHub.")
public class GitHubRestConfigController {

    private final GitHubRestConfigCreationService gitHubRestConfigCreationService;

    public GitHubRestConfigController(GitHubRestConfigCreationService gitHubRestConfigCreationService) {
        this.gitHubRestConfigCreationService = gitHubRestConfigCreationService;
    }

    @PostMapping("/github-rest-config")
    @Operation(summary = "Enregistrer un paramétrage GitHub REST", description = "Enregistre un identifiant, un token et un commentaire de connexion vers GitHub REST.")
    public ResponseEntity<GitHubRestConfig> create(@RequestBody GitHubRestConfigCreationForm form) {
        return ResponseEntity.ok(gitHubRestConfigCreationService.createGitHubRestConfig(form));
    }

    @GetMapping("/github-rest-config/{identifier}")
    @Operation(summary = "Lire un paramétrage GitHub REST", description = "Récupère un paramétrage GitHub REST par son identifiant fonctionnel (token masqué).")
    public ResponseEntity<GitHubRestConfig> getByIdentifier(@PathVariable String identifier) {
        return ResponseEntity.ok(gitHubRestConfigCreationService.getByIdentifier(identifier));
    }
}
