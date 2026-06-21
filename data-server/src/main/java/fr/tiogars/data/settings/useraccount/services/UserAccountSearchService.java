package fr.tiogars.data.settings.useraccount.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import fr.tiogars.data.settings.useraccount.entities.UserAccountEntity;
import fr.tiogars.data.settings.useraccount.models.UserAccount;
import fr.tiogars.data.settings.useraccount.models.UserAccountSearchResponse;
import fr.tiogars.data.settings.useraccount.repositories.UserAccountRepository;

@Service
public class UserAccountSearchService {

    private final UserAccountRepository userAccountRepository;

    public UserAccountSearchService(UserAccountRepository userAccountRepository) {
        this.userAccountRepository = userAccountRepository;
    }

    public UserAccountSearchResponse searchUserAccounts(int page, int size, String query) {
        String normalizedQuery = normalizeQuery(query);

        Pageable pageable = PageRequest.of(
            page,
            size,
            Sort.by(Sort.Order.asc("username"))
        );

        Page<UserAccountEntity> result = userAccountRepository.findAll(createSearchSpecification(normalizedQuery), pageable);

        List<UserAccount> items = result.getContent().stream()
            .map(UserAccountModelMapper::toModel)
            .toList();

        return new UserAccountSearchResponse(items, toSafeCount(result.getTotalElements()), page, size, normalizedQuery);
    }

    private Specification<UserAccountEntity> createSearchSpecification(String query) {
        if (query == null) {
            return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.conjunction();
        }

        String likePattern = "%" + query.toLowerCase() + "%";

        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.or(
            criteriaBuilder.like(criteriaBuilder.lower(root.get("username")), likePattern),
            criteriaBuilder.like(criteriaBuilder.lower(root.get("role")), likePattern)
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
