package fr.tiogars.data.dev.docs.githubrepository.services;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import fr.tiogars.data.common.exceptions.DataNotFoundException;
import fr.tiogars.data.dev.docs.githubrepository.entities.GitHubRepositoryEntity;
import fr.tiogars.data.dev.docs.githubrepository.models.GitHubRepository;
import fr.tiogars.data.dev.docs.githubrepository.models.GitHubRepositoryBulkSyncResult;
import fr.tiogars.data.dev.docs.githubrepository.models.GitHubRepositorySyncResult;
import fr.tiogars.data.dev.docs.githubrepository.repositories.GitHubRepositoryRepository;
import fr.tiogars.data.dev.docs.githubrestconfig.entities.GitHubRestConfigEntity;
import fr.tiogars.data.dev.docs.githubrestconfig.repositories.GitHubRestConfigRepository;

@Service
public class GitHubRepositorySyncService {

    private static final String GITHUB_ACCEPT_HEADER = "application/vnd.github+json";
    private static final String GITHUB_API_VERSION = "2022-11-28";

    private final GitHubRepositoryRepository gitHubRepositoryRepository;
    private final GitHubRestConfigRepository gitHubRestConfigRepository;
    private final RestClient gitHubRestClient;

    public GitHubRepositorySyncService(
        GitHubRepositoryRepository gitHubRepositoryRepository,
        GitHubRestConfigRepository gitHubRestConfigRepository,
        RestClient.Builder restClientBuilder
    ) {
        this.gitHubRepositoryRepository = gitHubRepositoryRepository;
        this.gitHubRestConfigRepository = gitHubRestConfigRepository;
        this.gitHubRestClient = restClientBuilder.baseUrl("https://api.github.com").build();
    }

    public GitHubRepositorySyncResult syncRepository(String ownerInput, String nameInput, String configIdentifierInput) {
        String owner = GitHubRepositoryCreationService.requireText(ownerInput, "Le propriétaire est obligatoire.");
        String name = GitHubRepositoryCreationService.requireText(nameInput, "Le nom du repository est obligatoire.");
        String configIdentifier = GitHubRepositoryCreationService.requireText(
            configIdentifierInput,
            "L'identifiant de configuration GitHub REST est obligatoire.");

        GitHubRestConfigEntity config = gitHubRestConfigRepository.findByIdentifierIgnoreCase(configIdentifier)
            .orElseThrow(() -> new DataNotFoundException(
                "Paramétrage GitHub introuvable pour l'identifiant: " + configIdentifier));

        String fullName = GitHubRepositoryCreationService.buildFullName(owner, name);

        try {
            @SuppressWarnings("unchecked")
            Map<String, Object> responseBody = gitHubRestClient.get()
                .uri("/repos/{owner}/{name}", owner, name)
                .header("Authorization", "Bearer " + config.getToken())
                .header("Accept", GITHUB_ACCEPT_HEADER)
                .header("X-GitHub-Api-Version", GITHUB_API_VERSION)
                .retrieve()
                .body(Map.class);

            if (responseBody == null) {
                throw new IllegalArgumentException("Réponse GitHub vide pour le repository " + fullName + ".");
            }

            return upsertFromGitHubResponse(responseBody, config.getIdentifier());
        } catch (HttpClientErrorException.NotFound ex) {
            return markAsMissing(fullName, config.getIdentifier());
        }
    }

    private GitHubRepositorySyncResult upsertFromGitHubResponse(Map<String, Object> responseBody, String usedConfigIdentifier) {
        String owner = readNestedString(responseBody, "owner", "login", "Le propriétaire du repository est absent dans la réponse GitHub.");
        String name = readString(responseBody, "name", "Le nom du repository est absent dans la réponse GitHub.");
        String fullName = GitHubRepositoryCreationService.buildFullName(owner, name);

        GitHubRepositoryEntity entity = gitHubRepositoryRepository.findByFullNameIgnoreCase(fullName)
            .orElseGet(GitHubRepositoryEntity::new);
        boolean isCreation = entity.getId() == null;

        GitHubRepositoryCreationService.applyValues(
            entity,
            owner,
            name,
            readString(responseBody, "html_url", "L'URL du repository est absente dans la réponse GitHub."),
            readNullableString(responseBody, "description"),
            readNullableString(responseBody, "default_branch"),
            readNullableString(responseBody, "language"),
            readNullableInteger(responseBody, "stargazers_count"),
            readNullableBoolean(responseBody, "archived"));

        entity.setExistsOnGitHub(Boolean.TRUE);

        GitHubRepository saved = GitHubRepositoryModelMapper.toModel(gitHubRepositoryRepository.save(entity));
        GitHubRepositorySyncResult.SyncStatus status = isCreation
            ? GitHubRepositorySyncResult.SyncStatus.CREATED
            : GitHubRepositorySyncResult.SyncStatus.UPDATED;

        return new GitHubRepositorySyncResult(
            status,
            saved,
            usedConfigIdentifier,
            isCreation
                ? "Repository créé depuis GitHub et ajouté à la liste locale."
                : "Repository synchronisé avec les données GitHub.");
    }

    private GitHubRepositorySyncResult markAsMissing(String fullName, String usedConfigIdentifier) {
        GitHubRepositoryEntity entity = gitHubRepositoryRepository.findByFullNameIgnoreCase(fullName)
            .orElseThrow(() -> new DataNotFoundException(
                "Repository introuvable sur GitHub et absent de la liste locale: " + fullName));

        entity.setExistsOnGitHub(Boolean.FALSE);

        GitHubRepository saved = GitHubRepositoryModelMapper.toModel(gitHubRepositoryRepository.save(entity));
        return new GitHubRepositorySyncResult(
            GitHubRepositorySyncResult.SyncStatus.MARKED_AS_MISSING,
            saved,
            usedConfigIdentifier,
            "Repository marqué comme inexistant sur GitHub.");
    }

    private static String readString(Map<String, Object> source, String key, String errorMessage) {
        Object value = source.get(key);

        if (!(value instanceof String text) || text.isBlank()) {
            throw new IllegalArgumentException(errorMessage);
        }

        return text;
    }

    @SuppressWarnings("unchecked")
    private static String readNestedString(
        Map<String, Object> source,
        String parentKey,
        String childKey,
        String errorMessage
    ) {
        Object nested = source.get(parentKey);

        if (!(nested instanceof Map<?, ?> nestedMap)) {
            throw new IllegalArgumentException(errorMessage);
        }

        Object value = ((Map<String, Object>) nestedMap).get(childKey);
        if (!(value instanceof String text) || text.isBlank()) {
            throw new IllegalArgumentException(errorMessage);
        }

        return text;
    }

    public GitHubRepositoryBulkSyncResult syncAllRepositories(String configIdentifierInput) {
        String configIdentifier = GitHubRepositoryCreationService.requireText(
            configIdentifierInput,
            "L'identifiant de configuration GitHub REST est obligatoire.");

        GitHubRestConfigEntity config = gitHubRestConfigRepository.findByIdentifierIgnoreCase(configIdentifier)
            .orElseThrow(() -> new DataNotFoundException(
                "Paramétrage GitHub introuvable pour l'identifiant: " + configIdentifier));

        Set<String> syncedFullNames = new HashSet<>();
        int created = 0;
        int updated = 0;
        int page = 1;

        while (true) {
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> pageItems = gitHubRestClient.get()
                .uri("/user/repos?per_page=100&page={page}", page)
                .header("Authorization", "Bearer " + config.getToken())
                .header("Accept", GITHUB_ACCEPT_HEADER)
                .header("X-GitHub-Api-Version", GITHUB_API_VERSION)
                .retrieve()
                .body(List.class);

            if (pageItems == null || pageItems.isEmpty()) {
                break;
            }

            for (Map<String, Object> repoData : pageItems) {
                GitHubRepositorySyncResult result = upsertFromGitHubResponse(repoData, config.getIdentifier());
                syncedFullNames.add(result.getRepository().getFullName().toLowerCase());

                if (result.getStatus() == GitHubRepositorySyncResult.SyncStatus.CREATED) {
                    created++;
                } else {
                    updated++;
                }
            }

            page++;
        }

        List<GitHubRepositoryEntity> allLocal = gitHubRepositoryRepository.findAll();
        List<GitHubRepositoryEntity> toMarkMissing = new ArrayList<>();

        for (GitHubRepositoryEntity entity : allLocal) {
            String localFullName = entity.getFullName() != null ? entity.getFullName().toLowerCase() : "";
            if (!syncedFullNames.contains(localFullName)) {
                toMarkMissing.add(entity);
            }
        }

        for (GitHubRepositoryEntity entity : toMarkMissing) {
            entity.setExistsOnGitHub(Boolean.FALSE);
        }
        gitHubRepositoryRepository.saveAll(toMarkMissing);

        int total = created + updated + toMarkMissing.size();
        return new GitHubRepositoryBulkSyncResult(created, updated, toMarkMissing.size(), total, config.getIdentifier());
    }

    private static String readNullableString(Map<String, Object> source, String key) {
        Object value = source.get(key);
        return value instanceof String text && !text.isBlank() ? text : null;
    }

    private static Integer readNullableInteger(Map<String, Object> source, String key) {
        Object value = source.get(key);

        if (value instanceof Number number) {
            return number.intValue();
        }

        return null;
    }

    private static Boolean readNullableBoolean(Map<String, Object> source, String key) {
        Object value = source.get(key);

        if (value instanceof Boolean bool) {
            return bool;
        }

        return null;
    }
}
