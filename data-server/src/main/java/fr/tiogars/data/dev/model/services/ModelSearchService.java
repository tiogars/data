package fr.tiogars.data.dev.model.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import fr.tiogars.data.dev.model.entities.ModelEntity;
import fr.tiogars.data.dev.model.models.Model;
import fr.tiogars.data.dev.model.models.ModelSearchResponse;
import fr.tiogars.data.dev.model.repositories.ModelRepository;

@Service
public class ModelSearchService {

    private final ModelRepository modelRepository;

    public ModelSearchService(ModelRepository modelRepository) {
        this.modelRepository = modelRepository;
    }

    public ModelSearchResponse searchModels(int page, int size, String query) {
        String normalizedQuery = normalizeQuery(query);

        Pageable pageable = PageRequest.of(
            page,
            size,
            Sort.by(Sort.Order.asc("name"))
        );

        Page<ModelEntity> result = modelRepository.findAll(createSearchSpecification(normalizedQuery), pageable);

        List<Model> items = result.getContent().stream()
            .map(ModelMapper::toModel)
            .toList();

        return new ModelSearchResponse(items, toSafeCount(result.getTotalElements()), page, size, normalizedQuery);
    }

    private Specification<ModelEntity> createSearchSpecification(String query) {
        if (query == null) {
            return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.conjunction();
        }

        String likePattern = "%" + query.toLowerCase() + "%";

        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.or(
            criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), likePattern),
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
