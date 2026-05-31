package fr.tiogars.data.dev.githubrepository.services;

import org.springframework.stereotype.Service;

import fr.tiogars.data.dev.githubrepository.entities.GitHubRepositoryEntity;
import fr.tiogars.data.dev.githubrepository.forms.GitHubRepositoryCreationForm;
import fr.tiogars.data.dev.githubrepository.models.GitHubRepository;
import fr.tiogars.data.dev.githubrepository.repositories.GitHubRepositoryRepository;

@Service
public class GitHubRepositoryCreationService {

    private final GitHubRepositoryRepository gitHubRepositoryRepository;

    public GitHubRepositoryCreationService(GitHubRepositoryRepository gitHubRepositoryRepository) {
        this.gitHubRepositoryRepository = gitHubRepositoryRepository;
    }

    public GitHubRepository createGitHubRepository(GitHubRepositoryCreationForm form) {
        String fullName = buildFullName(form.getOwner(), form.getName());
        validateUniqueFullName(fullName, null);

        GitHubRepositoryEntity entity = new GitHubRepositoryEntity();
        applyValues(
            entity,
            form.getOwner(),
            form.getName(),
            form.getUrl(),
            form.getDescription(),
            form.getDefaultBranch(),
            form.getLanguage(),
            form.getStars(),
            form.getArchived());

        return GitHubRepositoryModelMapper.toModel(gitHubRepositoryRepository.save(entity));
    }

    static void applyValues(
        GitHubRepositoryEntity entity,
        String owner,
        String name,
        String url,
        String description,
        String defaultBranch,
        String language,
        Integer stars,
        Boolean archived
    ) {
        String trimmedOwner = requireText(owner, "Le propriétaire est obligatoire.");
        String trimmedName = requireText(name, "Le nom du repository est obligatoire.");

        entity.setOwner(trimmedOwner);
        entity.setName(trimmedName);
        entity.setFullName(buildFullName(trimmedOwner, trimmedName));
        entity.setUrl(requireUrl(url));
        entity.setDescription(normalizeNullableText(description));
        entity.setDefaultBranch(normalizeDefaultBranch(defaultBranch));
        entity.setLanguage(normalizeNullableText(language));
        entity.setStars(normalizeStars(stars));
        entity.setArchived(archived != null ? archived : Boolean.FALSE);
    }

    void validateUniqueFullName(String fullName, String currentId) {
        gitHubRepositoryRepository.findByFullNameIgnoreCase(fullName)
            .filter(entity -> !entity.getId().equals(currentId))
            .ifPresent(entity -> {
                throw new IllegalArgumentException("Un repository avec ce owner/name existe déjà.");
            });
    }

    static String buildFullName(String owner, String name) {
        return requireText(owner, "Le propriétaire est obligatoire.") + "/" + requireText(name, "Le nom du repository est obligatoire.");
    }

    static String requireText(String value, String message) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(message);
        }

        return value.trim();
    }

    static String normalizeNullableText(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }

        return value.trim();
    }

    static String normalizeDefaultBranch(String value) {
        if (value == null || value.isBlank()) {
            return "main";
        }

        return value.trim();
    }

    static int normalizeStars(Integer value) {
        int normalized = value != null ? value : 0;

        if (normalized < 0) {
            throw new IllegalArgumentException("Le nombre d'étoiles ne peut pas être négatif.");
        }

        return normalized;
    }

    static String requireUrl(String value) {
        String trimmed = requireText(value, "L'URL est obligatoire.");
        String lower = trimmed.toLowerCase();

        if (!lower.startsWith("http://") && !lower.startsWith("https://")) {
            throw new IllegalArgumentException("L'URL doit commencer par http:// ou https://.");
        }

        return trimmed;
    }
}
