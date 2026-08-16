package fr.tiogars.data.products.gtin.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.core.TypedPropertyPath;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import fr.tiogars.data.products.gtin.entities.GtinEntity;
import fr.tiogars.data.products.gtin.models.Gtin;
import fr.tiogars.data.products.gtin.models.GtinSearchResponse;
import fr.tiogars.data.products.gtin.repositories.GtinRepository;

@Service
public class GtinSearchService {

    private final GtinRepository gtinRepository;

    public GtinSearchService(GtinRepository gtinRepository) {
        this.gtinRepository = gtinRepository;
    }

    public GtinSearchResponse searchGtins(int page, int size, String query) {
        String normalizedQuery = normalizeQuery(query);

        Pageable pageable = PageRequest.of(
            page,
            size,
            Sort.by(TypedPropertyPath.of(GtinSearchService::getCode)).ascending()
        );

        Page<GtinEntity> result = gtinRepository.findAll(createSearchSpecification(normalizedQuery), pageable);

        List<Gtin> items = result.getContent().stream()
            .map(GtinModelMapper::toModel)
            .toList();

        return new GtinSearchResponse(items, toSafeCount(result.getTotalElements()), page, size, normalizedQuery);
    }

    private static String getCode(@NonNull GtinEntity entity) {
        return entity.getCode();
    }

    private Specification<GtinEntity> createSearchSpecification(String query) {
        if (query == null) {
            return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.conjunction();
        }

        String likePattern = "%" + query.toLowerCase() + "%";

        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.or(
            criteriaBuilder.like(criteriaBuilder.lower(root.get("code")), likePattern),
            criteriaBuilder.like(criteriaBuilder.lower(criteriaBuilder.coalesce(root.get("description"), "")), likePattern)
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
