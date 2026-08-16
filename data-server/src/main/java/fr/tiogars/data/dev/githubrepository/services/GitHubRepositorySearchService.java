package fr.tiogars.data.dev.githubrepository.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.core.TypedPropertyPath;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import fr.tiogars.data.dev.githubrepository.entities.GitHubRepositoryEntity;
import fr.tiogars.data.dev.githubrepository.models.GitHubRepository;
import fr.tiogars.data.dev.githubrepository.models.GitHubRepositoryListResponse;
import fr.tiogars.data.dev.githubrepository.repositories.GitHubRepositoryRepository;

@Service
public class GitHubRepositorySearchService {

    private final GitHubRepositoryRepository gitHubRepositoryRepository;

    public GitHubRepositorySearchService(GitHubRepositoryRepository gitHubRepositoryRepository) {
        this.gitHubRepositoryRepository = gitHubRepositoryRepository;
    }

    public GitHubRepositoryListResponse searchGitHubRepositories(int page, int size, String query) {
        String normalizedQuery = normalizeQuery(query);

        Pageable pageable = PageRequest.of(
            page,
            size,
            Sort.by(TypedPropertyPath.of(GitHubRepositorySearchService::getStars)).descending()
                .and(Sort.by(TypedPropertyPath.of(GitHubRepositorySearchService::getFullName)).ascending())
        );

        Page<GitHubRepositoryEntity> result = gitHubRepositoryRepository.findAll(
            createSearchSpecification(normalizedQuery),
            pageable
        );

        List<GitHubRepository> items = result.getContent().stream()
            .map(GitHubRepositoryModelMapper::toModel)
            .toList();

        return new GitHubRepositoryListResponse(
            items,
            toSafeCount(result.getTotalElements()),
            page,
            size,
            normalizedQuery
        );
    }

    private static Integer getStars(@NonNull GitHubRepositoryEntity entity) {
        return entity.getStars();
    }

    private static String getFullName(@NonNull GitHubRepositoryEntity entity) {
        return entity.getFullName();
    }

    private Specification<GitHubRepositoryEntity> createSearchSpecification(String query) {
        if (query == null) {
            return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.conjunction();
        }

        String likePattern = "%" + query.toLowerCase() + "%";

        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.or(
            criteriaBuilder.like(criteriaBuilder.lower(root.get("owner")), likePattern),
            criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), likePattern),
            criteriaBuilder.like(criteriaBuilder.lower(root.get("fullName")), likePattern),
            criteriaBuilder.like(criteriaBuilder.lower(root.get("url")), likePattern),
            criteriaBuilder.like(criteriaBuilder.lower(criteriaBuilder.coalesce(root.get("description"), "")), likePattern),
            criteriaBuilder.like(criteriaBuilder.lower(root.get("defaultBranch")), likePattern),
            criteriaBuilder.like(criteriaBuilder.lower(criteriaBuilder.coalesce(root.get("language"), "")), likePattern)
        );
    }

    private String normalizeQuery(String query) {
        if (query == null || query.isBlank()) {
            return null;
        }

        return query.trim();
    }

    private int toSafeCount(long value) {
        return value > Integer.MAX_VALUE ? Integer.MAX_VALUE : (int) value;
    }
}