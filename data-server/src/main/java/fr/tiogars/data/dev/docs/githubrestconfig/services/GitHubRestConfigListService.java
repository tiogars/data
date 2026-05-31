package fr.tiogars.data.dev.docs.githubrestconfig.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import fr.tiogars.data.dev.docs.githubrestconfig.entities.GitHubRestConfigEntity;
import fr.tiogars.data.dev.docs.githubrestconfig.models.GitHubRestConfig;
import fr.tiogars.data.dev.docs.githubrestconfig.models.GitHubRestConfigListResponse;
import fr.tiogars.data.dev.docs.githubrestconfig.repositories.GitHubRestConfigRepository;

@Service
public class GitHubRestConfigListService {

    private final GitHubRestConfigRepository gitHubRestConfigRepository;

    public GitHubRestConfigListService(GitHubRestConfigRepository gitHubRestConfigRepository) {
        this.gitHubRestConfigRepository = gitHubRestConfigRepository;
    }

    public GitHubRestConfigListResponse listGitHubRestConfigs(int page, int size, String query) {
        String normalizedQuery = normalizeQuery(query);

        Pageable pageable = PageRequest.of(
            page,
            size,
            Sort.by(Sort.Order.asc("identifier"))
        );

        Page<GitHubRestConfigEntity> result = normalizedQuery == null
            ? gitHubRestConfigRepository.findAll(pageable)
            : gitHubRestConfigRepository.findByIdentifierContainingIgnoreCaseOrCommentContainingIgnoreCase(
                normalizedQuery,
                normalizedQuery,
                pageable
            );

        List<GitHubRestConfig> items = result.getContent().stream()
            .map(GitHubRestConfigModelMapper::toModel)
            .toList();

        return new GitHubRestConfigListResponse(
            items,
            toSafeCount(result.getTotalElements()),
            page,
            size,
            normalizedQuery
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
