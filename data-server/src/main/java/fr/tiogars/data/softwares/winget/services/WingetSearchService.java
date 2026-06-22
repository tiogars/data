package fr.tiogars.data.softwares.winget.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import fr.tiogars.data.softwares.winget.entities.WingetEntity;
import fr.tiogars.data.softwares.winget.models.Winget;
import fr.tiogars.data.softwares.winget.models.WingetSearchResponse;
import fr.tiogars.data.softwares.winget.repositories.WingetRepository;

@Service
public class WingetSearchService {

    private final WingetRepository wingetRepository;

    public WingetSearchService(WingetRepository wingetRepository) {
        this.wingetRepository = wingetRepository;
    }

    public WingetSearchResponse searchWingets(int page, int size, String query) {
        String normalizedQuery = normalizeQuery(query);

        Pageable pageable = PageRequest.of(
            page,
            size,
            Sort.by(Sort.Order.asc("name"))
        );

        Page<WingetEntity> result = wingetRepository.findAll(createSearchSpecification(normalizedQuery), pageable);

        List<Winget> items = result.getContent().stream()
            .map(WingetModelMapper::toModel)
            .toList();

        return new WingetSearchResponse(items, toSafeCount(result.getTotalElements()), page, size, normalizedQuery);
    }

    private Specification<WingetEntity> createSearchSpecification(String query) {
        if (query == null) {
            return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.conjunction();
        }

        String likePattern = "%" + query.toLowerCase() + "%";

        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.or(
            criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), likePattern),
            criteriaBuilder.like(criteriaBuilder.lower(root.get("wingetId")), likePattern),
            criteriaBuilder.like(criteriaBuilder.lower(root.get("installCommand")), likePattern),
            criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), likePattern)
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
