package fr.tiogars.data.dev.githubrestconfig.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.dev.githubrestconfig.forms.GitHubRestConfigCreationForm;
import fr.tiogars.data.dev.githubrestconfig.forms.GitHubRestConfigUpdateForm;
import fr.tiogars.data.dev.githubrestconfig.models.GitHubRestConfig;
import fr.tiogars.data.dev.githubrestconfig.models.GitHubRestConfigListResponse;
import fr.tiogars.data.dev.githubrestconfig.services.GitHubRestConfigCreationService;
import fr.tiogars.data.dev.githubrestconfig.services.GitHubRestConfigDeleteOneService;
import fr.tiogars.data.dev.githubrestconfig.services.GitHubRestConfigGetOneService;
import fr.tiogars.data.dev.githubrestconfig.services.GitHubRestConfigSearchService;
import fr.tiogars.data.dev.githubrestconfig.services.GitHubRestConfigUpdateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "github-rest-config", description = "Paramétrage de la connexion REST vers GitHub.")
public class GitHubRestConfigController {

    private final GitHubRestConfigCreationService gitHubRestConfigCreationService;
    private final GitHubRestConfigSearchService gitHubRestConfigSearchService;
    private final GitHubRestConfigGetOneService gitHubRestConfigGetOneService;
    private final GitHubRestConfigUpdateService gitHubRestConfigUpdateService;
    private final GitHubRestConfigDeleteOneService gitHubRestConfigDeleteOneService;

    public GitHubRestConfigController(
        GitHubRestConfigCreationService gitHubRestConfigCreationService,
        GitHubRestConfigSearchService gitHubRestConfigSearchService,
        GitHubRestConfigGetOneService gitHubRestConfigGetOneService,
        GitHubRestConfigUpdateService gitHubRestConfigUpdateService,
        GitHubRestConfigDeleteOneService gitHubRestConfigDeleteOneService
    ) {
        this.gitHubRestConfigCreationService = gitHubRestConfigCreationService;
        this.gitHubRestConfigSearchService = gitHubRestConfigSearchService;
        this.gitHubRestConfigGetOneService = gitHubRestConfigGetOneService;
        this.gitHubRestConfigUpdateService = gitHubRestConfigUpdateService;
        this.gitHubRestConfigDeleteOneService = gitHubRestConfigDeleteOneService;
    }

    @GetMapping("/github-rest-config/search")
    @Operation(summary = "Rechercher les parametrages GitHub REST", description = "Retourne les configurations token GitHub avec pagination et filtre textuel.")
    public ResponseEntity<GitHubRestConfigListResponse> searchGitHubRestConfigs(
        @Parameter(description = "Index de page (commence a 0).", example = "0")
        @RequestParam(defaultValue = "0") int page,
        @Parameter(description = "Nombre d'elements par page.", example = "10")
        @RequestParam(defaultValue = "10") int size,
        @Parameter(description = "Texte libre de recherche (identifiant et commentaire).", example = "integration")
        @RequestParam(required = false) String q
    ) {
        if (page < 0) {
            throw new IllegalArgumentException("Le parametre page doit etre superieur ou egal a 0.");
        }

        if (size <= 0) {
            throw new IllegalArgumentException("Le parametre size doit etre superieur a 0.");
        }

        if (size > 100) {
            throw new IllegalArgumentException("Le parametre size ne peut pas depasser 100.");
        }

        return ResponseEntity.ok(gitHubRestConfigSearchService.searchGitHubRestConfigs(page, size, q));
    }

    @PostMapping("/github-rest-config")
    @Operation(summary = "Enregistrer un parametrage GitHub REST", description = "Enregistre un identifiant, un token et un commentaire de connexion vers GitHub REST.")
    public ResponseEntity<GitHubRestConfig> create(@RequestBody GitHubRestConfigCreationForm form) {
        return ResponseEntity.ok(gitHubRestConfigCreationService.createGitHubRestConfig(form));
    }

    @GetMapping("/github-rest-config/{identifier}")
    @Operation(summary = "Lire un parametrage GitHub REST", description = "Recupere un parametrage GitHub REST par son identifiant fonctionnel (token masque).")
    public ResponseEntity<GitHubRestConfig> getByIdentifier(@PathVariable String identifier) {
        return ResponseEntity.ok(gitHubRestConfigGetOneService.getByIdentifier(identifier));
    }

    @PutMapping("/github-rest-config/{identifier}")
    @Operation(summary = "Modifier un parametrage GitHub REST", description = "Met a jour l'identifiant, le token (optionnel) et le commentaire d'un parametrage GitHub REST.")
    public ResponseEntity<GitHubRestConfig> updateByIdentifier(@PathVariable String identifier, @RequestBody GitHubRestConfigUpdateForm form) {
        return ResponseEntity.ok(gitHubRestConfigUpdateService.updateGitHubRestConfig(identifier, form));
    }

    @DeleteMapping("/github-rest-config/{identifier}")
    @Operation(summary = "Supprimer un parametrage GitHub REST", description = "Supprime un parametrage GitHub REST a partir de son identifiant fonctionnel.")
    public ResponseEntity<Void> deleteByIdentifier(@PathVariable String identifier) {
        gitHubRestConfigDeleteOneService.deleteByIdentifier(identifier);
        return ResponseEntity.noContent().build();
    }
}
