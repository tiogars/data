package fr.tiogars.data.docs.section.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import fr.tiogars.data.docs.section.entities.SectionEntity;
import fr.tiogars.data.docs.section.models.Section;
import fr.tiogars.data.docs.section.models.SectionSearchResponse;
import fr.tiogars.data.docs.section.repositories.SectionRepository;

@Service
public class SectionSearchService {

    private final SectionRepository sectionRepository;

    public SectionSearchService(SectionRepository sectionRepository) {
        this.sectionRepository = sectionRepository;
    }

    public SectionSearchResponse searchSections(int page, int size, String query) {
        String normalizedQuery = normalizeQuery(query);

        Pageable pageable = PageRequest.of(
            page,
            size,
            Sort.by(Sort.Order.asc("name"))
        );

        Page<SectionEntity> result = sectionRepository.findAll(createSearchSpecification(normalizedQuery), pageable);

        List<Section> items = result.getContent().stream()
            .map(SectionModelMapper::toSectionModel)
            .toList();

        return new SectionSearchResponse(items, toSafeCount(result.getTotalElements()), page, size, normalizedQuery);
    }

    private Specification<SectionEntity> createSearchSpecification(String query) {
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
