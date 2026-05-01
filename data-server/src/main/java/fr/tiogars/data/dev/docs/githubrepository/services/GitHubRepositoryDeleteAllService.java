package fr.tiogars.data.dev.docs.githubrepository.services;

import org.springframework.stereotype.Service;

import fr.tiogars.data.dev.docs.githubrepository.repositories.GitHubRepositoryRepository;

@Service
public class GitHubRepositoryDeleteAllService {

    private final GitHubRepositoryRepository gitHubRepositoryRepository;

    public GitHubRepositoryDeleteAllService(GitHubRepositoryRepository gitHubRepositoryRepository) {
        this.gitHubRepositoryRepository = gitHubRepositoryRepository;
    }

    public void deleteAllGitHubRepositories() {
        gitHubRepositoryRepository.deleteAll();
    }
}
