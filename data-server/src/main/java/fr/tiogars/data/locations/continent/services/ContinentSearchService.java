package fr.tiogars.data.locations.continent.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.core.TypedPropertyPath;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.jspecify.annotations.NonNull;

import fr.tiogars.data.locations.continent.entities.ContinentEntity;
import fr.tiogars.data.locations.continent.models.Continent;
import fr.tiogars.data.locations.continent.models.ContinentSearchResponse;
import fr.tiogars.data.locations.continent.repositories.ContinentRepository;

@Service
public class ContinentSearchService {

    private final ContinentRepository continentRepository;

    public ContinentSearchService(ContinentRepository continentRepository) {
        this.continentRepository = continentRepository;
    }

    public ContinentSearchResponse searchContinents(int page, int size, String query) {
        String normalizedQuery = normalizeQuery(query);

        Pageable pageable = PageRequest.of(
            page,
            size,
            Sort.by(TypedPropertyPath.of(ContinentSearchService::getContinentName)).ascending()
        );

        Page<ContinentEntity> result = continentRepository.findAll(createSearchSpecification(normalizedQuery), pageable);

        List<Continent> items = result.getContent().stream()
            .map(ContinentModelMapper::toModel)
            .toList();

        return new ContinentSearchResponse(items, toSafeCount(result.getTotalElements()), page, size, normalizedQuery);
    }

    private static String getContinentName(@NonNull ContinentEntity entity) {
        return entity.getName();
    }

    private Specification<ContinentEntity> createSearchSpecification(String query) {
        if (query == null) {
            return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.conjunction();
        }

        String likePattern = "%" + query.toLowerCase() + "%";

        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.or(
            criteriaBuilder.like(criteriaBuilder.lower(root.get("code")), likePattern),
            criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), likePattern)
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
