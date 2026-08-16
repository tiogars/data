package fr.tiogars.data.docs.section.services;

import java.util.List;
import java.util.ArrayList;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import jakarta.persistence.criteria.Predicate;

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

    public SectionSearchResponse searchSections(int page, int size, String query, String documentId) {
        String normalizedQuery = normalizeQuery(query);

        Pageable pageable = PageRequest.of(
            page,
            size,
            SectionRepository.DEFAULT_SECTION_SORT
        );

        Page<SectionEntity> result = sectionRepository.findAll(createSearchSpecification(normalizedQuery, documentId), pageable);

        List<Section> items = result.getContent().stream()
            .map(SectionModelMapper::toSectionModel)
            .toList();

        return new SectionSearchResponse(items, toSafeCount(result.getTotalElements()), page, size, normalizedQuery);
    }

    private Specification<SectionEntity> createSearchSpecification(String query, String documentId) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (documentId != null && !documentId.isBlank()) {
                predicates.add(criteriaBuilder.equal(root.get("document").get("id"), documentId));
            }

            if (query != null) {
                String likePattern = "%" + query.toLowerCase() + "%";
                predicates.add(criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), likePattern),
                    criteriaBuilder.like(criteriaBuilder.lower(criteriaBuilder.coalesce(root.get("description"), "")), likePattern)
                ));
            }

            if (predicates.isEmpty()) {
                return criteriaBuilder.conjunction();
            }

            return criteriaBuilder.and(predicates.toArray(Predicate[]::new));
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
