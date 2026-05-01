package fr.tiogars.data.dev.docs.githubrepository.services;

import org.springframework.stereotype.Service;

import fr.tiogars.data.common.exceptions.DataNotFoundException;
import fr.tiogars.data.dev.docs.githubrepository.repositories.GitHubRepositoryRepository;

@Service
public class GitHubRepositoryDeleteOneService {

    private final GitHubRepositoryRepository gitHubRepositoryRepository;

    public GitHubRepositoryDeleteOneService(GitHubRepositoryRepository gitHubRepositoryRepository) {
        this.gitHubRepositoryRepository = gitHubRepositoryRepository;
    }

    public void deleteGitHubRepositoryById(String id) {
        if (!gitHubRepositoryRepository.existsById(id)) {
            throw new DataNotFoundException("Repository GitHub non trouvé pour l'id: " + id);
        }

        gitHubRepositoryRepository.deleteById(id);
    }
}
