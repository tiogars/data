package fr.tiogars.data.dev.githubrestconfig.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.tiogars.data.common.exceptions.DataNotFoundException;
import fr.tiogars.data.dev.githubrestconfig.entities.GitHubRestConfigEntity;
import fr.tiogars.data.dev.githubrestconfig.repositories.GitHubRestConfigRepository;

@Service
public class GitHubRestConfigDeleteOneService {

    private final GitHubRestConfigRepository gitHubRestConfigRepository;

    public GitHubRestConfigDeleteOneService(GitHubRestConfigRepository gitHubRestConfigRepository) {
        this.gitHubRestConfigRepository = gitHubRestConfigRepository;
    }

    @Transactional
    public void deleteByIdentifier(String identifier) {
        String normalizedIdentifier = requireText(identifier, "L'identifiant est obligatoire.");

        GitHubRestConfigEntity entity = gitHubRestConfigRepository.findByIdentifierIgnoreCase(normalizedIdentifier)
            .orElseThrow(() -> new DataNotFoundException("Parametrage GitHub introuvable pour l'identifiant: " + normalizedIdentifier));

        gitHubRestConfigRepository.delete(entity);
    }

    private static String requireText(String value, String message) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(message);
        }

        return value.trim();
    }
}
