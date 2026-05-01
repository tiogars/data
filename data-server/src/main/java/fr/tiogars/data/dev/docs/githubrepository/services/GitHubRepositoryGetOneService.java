package fr.tiogars.data.dev.docs.githubrepository.services;

import org.springframework.stereotype.Service;

import fr.tiogars.data.common.exceptions.DataNotFoundException;
import fr.tiogars.data.dev.docs.githubrepository.models.GitHubRepository;
import fr.tiogars.data.dev.docs.githubrepository.repositories.GitHubRepositoryRepository;

@Service
public class GitHubRepositoryGetOneService {

    private final GitHubRepositoryRepository gitHubRepositoryRepository;

    public GitHubRepositoryGetOneService(GitHubRepositoryRepository gitHubRepositoryRepository) {
        this.gitHubRepositoryRepository = gitHubRepositoryRepository;
    }

    public GitHubRepository getGitHubRepositoryById(String id) {
        return gitHubRepositoryRepository.findById(id)
            .map(GitHubRepositoryModelMapper::toModel)
            .orElseThrow(() -> new DataNotFoundException("Repository GitHub non trouvé pour l'id: " + id));
    }
}
