package fr.tiogars.data.dev.githubrepository.services;

import org.springframework.stereotype.Service;

import fr.tiogars.data.common.exceptions.DataNotFoundException;
import fr.tiogars.data.dev.githubrepository.entities.GitHubRepositoryEntity;
import fr.tiogars.data.dev.githubrepository.models.GitHubRepository;
import fr.tiogars.data.dev.githubrepository.repositories.GitHubRepositoryRepository;

@Service
public class GitHubRepositoryUpdateService {

    private final GitHubRepositoryRepository gitHubRepositoryRepository;
    private final GitHubRepositoryCreationService gitHubRepositoryCreationService;

    public GitHubRepositoryUpdateService(
        GitHubRepositoryRepository gitHubRepositoryRepository,
        GitHubRepositoryCreationService gitHubRepositoryCreationService
    ) {
        this.gitHubRepositoryRepository = gitHubRepositoryRepository;
        this.gitHubRepositoryCreationService = gitHubRepositoryCreationService;
    }

    public GitHubRepository updateGitHubRepository(String id, GitHubRepository repositoryUpdate) {
        GitHubRepositoryEntity entity = gitHubRepositoryRepository.findById(id)
            .orElseThrow(() -> new DataNotFoundException("Repository GitHub non trouvé pour l'id: " + id));

        String fullName = GitHubRepositoryCreationService.buildFullName(repositoryUpdate.getOwner(), repositoryUpdate.getName());
        gitHubRepositoryCreationService.validateUniqueFullName(fullName, id);

        GitHubRepositoryCreationService.applyValues(
            entity,
            repositoryUpdate.getOwner(),
            repositoryUpdate.getName(),
            repositoryUpdate.getUrl(),
            repositoryUpdate.getDescription(),
            repositoryUpdate.getDefaultBranch(),
            repositoryUpdate.getLanguage(),
            repositoryUpdate.getStars(),
            repositoryUpdate.getArchived());

        return GitHubRepositoryModelMapper.toModel(gitHubRepositoryRepository.save(entity));
    }
}
