package fr.tiogars.data.dev.docs.githubrestconfig.services;

import org.springframework.stereotype.Service;

import fr.tiogars.data.common.exceptions.DataNotFoundException;
import fr.tiogars.data.dev.docs.githubrestconfig.entities.GitHubRestConfigEntity;
import fr.tiogars.data.dev.docs.githubrestconfig.models.GitHubRestConfig;
import fr.tiogars.data.dev.docs.githubrestconfig.repositories.GitHubRestConfigRepository;

@Service
public class GitHubRestConfigGetOneService {

    private final GitHubRestConfigRepository gitHubRestConfigRepository;

    public GitHubRestConfigGetOneService(GitHubRestConfigRepository gitHubRestConfigRepository) {
        this.gitHubRestConfigRepository = gitHubRestConfigRepository;
    }

    public GitHubRestConfig getByIdentifier(String identifier) {
        String normalizedIdentifier = requireText(identifier, "L'identifiant est obligatoire.");

        GitHubRestConfigEntity entity = gitHubRestConfigRepository.findByIdentifierIgnoreCase(normalizedIdentifier)
            .orElseThrow(() -> new DataNotFoundException("Parametrage GitHub introuvable pour l'identifiant: " + normalizedIdentifier));

        return GitHubRestConfigModelMapper.toModel(entity);
    }

    private static String requireText(String value, String message) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(message);
        }

        return value.trim();
    }
}
