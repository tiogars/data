package fr.tiogars.data.dev.githubrepository.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import fr.tiogars.data.dev.githubrepository.entities.GitHubRepositoryEntity;

@Repository
public interface GitHubRepositoryRepository extends JpaRepository<GitHubRepositoryEntity, String>, JpaSpecificationExecutor<GitHubRepositoryEntity> {

    Optional<GitHubRepositoryEntity> findByFullNameIgnoreCase(String fullName);
}
