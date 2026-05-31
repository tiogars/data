package fr.tiogars.data.dev.githubrestconfig.repositories;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import fr.tiogars.data.dev.githubrestconfig.entities.GitHubRestConfigEntity;

public interface GitHubRestConfigRepository extends JpaRepository<GitHubRestConfigEntity, String> {

    Optional<GitHubRestConfigEntity> findByIdentifierIgnoreCase(String identifier);

    Page<GitHubRestConfigEntity> findByIdentifierContainingIgnoreCaseOrCommentContainingIgnoreCase(
        String identifier,
        String comment,
        Pageable pageable
    );
}
