package fr.tiogars.data.dev.githubrestconfig.services;

import static fr.tiogars.data.common.validation.TextValidationUtils.normalizeNullableText;
import static fr.tiogars.data.common.validation.TextValidationUtils.requireText;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.tiogars.data.dev.githubrestconfig.entities.GitHubRestConfigEntity;
import fr.tiogars.data.dev.githubrestconfig.forms.GitHubRestConfigCreationForm;
import fr.tiogars.data.dev.githubrestconfig.models.GitHubRestConfig;
import fr.tiogars.data.dev.githubrestconfig.repositories.GitHubRestConfigRepository;

@Service
public class GitHubRestConfigCreationService {

    private final GitHubRestConfigRepository gitHubRestConfigRepository;

    public GitHubRestConfigCreationService(GitHubRestConfigRepository gitHubRestConfigRepository) {
        this.gitHubRestConfigRepository = gitHubRestConfigRepository;
    }

    @Transactional
    public GitHubRestConfig createGitHubRestConfig(GitHubRestConfigCreationForm form) {
        String identifier = requireText(form.getIdentifier(), "L'identifiant est obligatoire.");
        validateIdentifierUniqueness(identifier);

        GitHubRestConfigEntity entity = new GitHubRestConfigEntity();
        entity.setIdentifier(identifier);
        entity.setToken(requireText(form.getToken(), "Le token est obligatoire."));
        entity.setComment(normalizeNullableText(form.getComment()));

        return GitHubRestConfigModelMapper.toModel(gitHubRestConfigRepository.save(entity));
    }

    private void validateIdentifierUniqueness(String identifier) {
        gitHubRestConfigRepository.findByIdentifierIgnoreCase(identifier)
            .ifPresent(existing -> {
                throw new IllegalArgumentException("Un paramétrage GitHub existe déjà pour cet identifiant.");
            });
    }
}
