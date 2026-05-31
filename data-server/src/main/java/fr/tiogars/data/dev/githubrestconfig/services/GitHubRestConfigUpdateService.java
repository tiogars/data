package fr.tiogars.data.dev.githubrestconfig.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.tiogars.data.common.exceptions.DataNotFoundException;
import fr.tiogars.data.dev.githubrestconfig.entities.GitHubRestConfigEntity;
import fr.tiogars.data.dev.githubrestconfig.forms.GitHubRestConfigUpdateForm;
import fr.tiogars.data.dev.githubrestconfig.models.GitHubRestConfig;
import fr.tiogars.data.dev.githubrestconfig.repositories.GitHubRestConfigRepository;

@Service
public class GitHubRestConfigUpdateService {

    private final GitHubRestConfigRepository gitHubRestConfigRepository;

    public GitHubRestConfigUpdateService(GitHubRestConfigRepository gitHubRestConfigRepository) {
        this.gitHubRestConfigRepository = gitHubRestConfigRepository;
    }

    @Transactional
    public GitHubRestConfig updateGitHubRestConfig(String currentIdentifier, GitHubRestConfigUpdateForm form) {
        String normalizedCurrentIdentifier = requireText(currentIdentifier, "L'identifiant est obligatoire.");
        GitHubRestConfigEntity entity = gitHubRestConfigRepository.findByIdentifierIgnoreCase(normalizedCurrentIdentifier)
            .orElseThrow(() -> new DataNotFoundException("Parametrage GitHub introuvable pour l'identifiant: " + normalizedCurrentIdentifier));

        String nextIdentifier = requireText(form.getIdentifier(), "L'identifiant est obligatoire.");
        validateIdentifierUniqueness(nextIdentifier, entity.getId());

        entity.setIdentifier(nextIdentifier);
        entity.setComment(normalizeNullableText(form.getComment()));

        if (form.getToken() != null && !form.getToken().isBlank()) {
            entity.setToken(requireText(form.getToken(), "Le token est obligatoire."));
        }

        return GitHubRestConfigModelMapper.toModel(gitHubRestConfigRepository.save(entity));
    }

    private void validateIdentifierUniqueness(String identifier, String currentId) {
        gitHubRestConfigRepository.findByIdentifierIgnoreCase(identifier)
            .ifPresent(existing -> {
                if (!existing.getId().equals(currentId)) {
                    throw new IllegalArgumentException("Un parametrage GitHub existe deja pour cet identifiant.");
                }
            });
    }

    private static String requireText(String value, String message) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(message);
        }

        return value.trim();
    }

    private static String normalizeNullableText(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }

        return value.trim();
    }
}
