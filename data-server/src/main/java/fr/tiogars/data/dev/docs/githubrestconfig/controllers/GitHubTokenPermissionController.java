package fr.tiogars.data.dev.docs.githubrestconfig.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.dev.docs.githubrestconfig.forms.GitHubTokenPermissionRequest;
import fr.tiogars.data.dev.docs.githubrestconfig.models.GitHubTokenPermissionResponse;
import fr.tiogars.data.dev.docs.githubrestconfig.services.GitHubTokenPermissionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Tag(name = "github-rest-config", description = "Paramétrage de la connexion REST vers GitHub.")
public class GitHubTokenPermissionController {

    private final GitHubTokenPermissionService gitHubTokenPermissionService;

    public GitHubTokenPermissionController(GitHubTokenPermissionService gitHubTokenPermissionService) {
        this.gitHubTokenPermissionService = gitHubTokenPermissionService;
    }

    @PostMapping("/github-rest-config/permissions")
    @Operation(summary = "Lister les droits GitHub requis", description = "Retourne les permissions minimales à attribuer au token GitHub en fonction des opérations à appeler.")
    public ResponseEntity<GitHubTokenPermissionResponse> listRequiredPermissions(@RequestBody GitHubTokenPermissionRequest request) {
        return ResponseEntity.ok(gitHubTokenPermissionService.resolveRequiredPermissions(request.getOperations()));
    }
}
