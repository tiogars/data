package fr.tiogars.data.dev.docs.githubrestconfig.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.tiogars.data.dev.docs.githubrestconfig.entities.GitHubRestConfigEntity;

public interface GitHubRestConfigRepository extends JpaRepository<GitHubRestConfigEntity, String> {

    Optional<GitHubRestConfigEntity> findByIdentifierIgnoreCase(String identifier);
}
