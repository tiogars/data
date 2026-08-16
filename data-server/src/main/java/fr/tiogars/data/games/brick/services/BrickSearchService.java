package fr.tiogars.data.games.brick.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.core.TypedPropertyPath;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import fr.tiogars.data.games.brick.entities.BrickEntity;
import fr.tiogars.data.games.brick.models.Brick;
import fr.tiogars.data.games.brick.models.BrickSearchResponse;
import fr.tiogars.data.games.brick.repositories.BrickRepository;

@Service
public class BrickSearchService {

    private final BrickRepository brickRepository;

    public BrickSearchService(BrickRepository brickRepository) {
        this.brickRepository = brickRepository;
    }

    public BrickSearchResponse searchBricks(int page, int size, String query) {
        String normalizedQuery = normalizeQuery(query);

        Pageable pageable = PageRequest.of(
            page,
            size,
            Sort.by(TypedPropertyPath.of(BrickSearchService::getNumber)).ascending()
        );

        Page<BrickEntity> result = brickRepository.findAll(createSearchSpecification(normalizedQuery), pageable);

        List<Brick> items = result.getContent().stream()
            .map(BrickModelMapper::toModel)
            .toList();

        return new BrickSearchResponse(items, toSafeCount(result.getTotalElements()), page, size, normalizedQuery);
    }

    private static String getNumber(@NonNull BrickEntity entity) {
        return entity.getNumber();
    }

    private Specification<BrickEntity> createSearchSpecification(String query) {
        if (query == null) {
            return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.conjunction();
        }

        String likePattern = "%" + query.toLowerCase() + "%";

        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.or(
            criteriaBuilder.like(criteriaBuilder.lower(root.get("number")), likePattern),
            criteriaBuilder.like(criteriaBuilder.lower(root.get("title")), likePattern),
            criteriaBuilder.like(criteriaBuilder.lower(criteriaBuilder.coalesce(root.get("tags"), "")), likePattern)
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
