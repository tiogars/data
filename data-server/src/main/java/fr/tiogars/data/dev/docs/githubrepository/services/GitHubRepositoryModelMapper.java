package fr.tiogars.data.dev.docs.githubrepository.services;

import fr.tiogars.data.dev.docs.githubrepository.entities.GitHubRepositoryEntity;
import fr.tiogars.data.dev.docs.githubrepository.models.GitHubRepository;

final class GitHubRepositoryModelMapper {

    private GitHubRepositoryModelMapper() {
    }

    static GitHubRepository toModel(GitHubRepositoryEntity entity) {
        GitHubRepository model = new GitHubRepository();
        model.setId(entity.getId());
        model.setOwner(entity.getOwner());
        model.setName(entity.getName());
        model.setFullName(entity.getFullName());
        model.setUrl(entity.getUrl());
        model.setDescription(entity.getDescription());
        model.setDefaultBranch(entity.getDefaultBranch());
        model.setLanguage(entity.getLanguage());
        model.setStars(entity.getStars());
        model.setArchived(entity.getArchived());
        model.setExistsOnGitHub(entity.getExistsOnGitHub() != null ? entity.getExistsOnGitHub() : Boolean.TRUE);
        return model;
    }
}
