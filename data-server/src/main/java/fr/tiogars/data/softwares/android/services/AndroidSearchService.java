package fr.tiogars.data.softwares.android.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import fr.tiogars.data.softwares.android.entities.AndroidEntity;
import fr.tiogars.data.softwares.android.models.Android;
import fr.tiogars.data.softwares.android.models.AndroidSearchResponse;
import fr.tiogars.data.softwares.android.repositories.AndroidRepository;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;

@Service
public class AndroidSearchService {

    private final AndroidRepository androidRepository;

    public AndroidSearchService(AndroidRepository androidRepository) {
        this.androidRepository = androidRepository;
    }

    public AndroidSearchResponse searchAndroids(int page, int size, String query) {
        String normalizedQuery = normalizeQuery(query);

        Pageable pageable = PageRequest.of(
            page,
            size,
            Sort.by(Sort.Order.asc("name"))
        );

        Page<AndroidEntity> result = androidRepository.findAll(createSearchSpecification(normalizedQuery), pageable);

        List<Android> items = result.getContent().stream()
            .map(AndroidModelMapper::toModel)
            .toList();

        return new AndroidSearchResponse(items, toSafeCount(result.getTotalElements()), page, size, normalizedQuery);
    }

    private Specification<AndroidEntity> createSearchSpecification(String query) {
        if (query == null) {
            return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.conjunction();
        }

        String likePattern = "%" + query.toLowerCase() + "%";

        return (root, criteriaQuery, criteriaBuilder) -> {
            criteriaQuery.distinct(true);
            Join<AndroidEntity, String> categoriesJoin = root.join("category", JoinType.LEFT);

            return criteriaBuilder.or(
                criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), likePattern),
                criteriaBuilder.like(criteriaBuilder.lower(root.get("packageName")), likePattern),
                criteriaBuilder.like(criteriaBuilder.lower(criteriaBuilder.coalesce(root.get("description"), "")), likePattern),
                criteriaBuilder.like(criteriaBuilder.lower(criteriaBuilder.coalesce(categoriesJoin, "")), likePattern)
            );
        };
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
