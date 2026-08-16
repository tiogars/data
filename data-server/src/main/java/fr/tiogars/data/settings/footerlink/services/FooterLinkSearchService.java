package fr.tiogars.data.settings.footerlink.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.core.TypedPropertyPath;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.jspecify.annotations.NonNull;

import fr.tiogars.data.settings.footerlink.entities.FooterLinkEntity;
import fr.tiogars.data.settings.footerlink.models.FooterLink;
import fr.tiogars.data.settings.footerlink.models.FooterLinkSearchResponse;
import fr.tiogars.data.settings.footerlink.repositories.FooterLinkRepository;

@Service
public class FooterLinkSearchService {

    private final FooterLinkRepository footerLinkRepository;

    public FooterLinkSearchService(FooterLinkRepository footerLinkRepository) {
        this.footerLinkRepository = footerLinkRepository;
    }

    public FooterLinkSearchResponse searchFooterLinks(int page, int size, String query) {
        String normalizedQuery = normalizeQuery(query);

        Pageable pageable = PageRequest.of(
            page,
            size,
            Sort.by(
                TypedPropertyPath.of(FooterLinkSearchService::getDisplayOrder),
                TypedPropertyPath.of(FooterLinkSearchService::getLabel)
            )
        );

        Page<FooterLinkEntity> result = footerLinkRepository.findAll(createSearchSpecification(normalizedQuery), pageable);

        List<FooterLink> items = result.getContent().stream()
            .map(FooterLinkModelMapper::toFooterLinkModel)
            .toList();

        return new FooterLinkSearchResponse(items, toSafeCount(result.getTotalElements()), page, size, normalizedQuery);
    }

    private static Integer getDisplayOrder(@NonNull FooterLinkEntity entity) {
        return entity.getDisplayOrder();
    }

    private static String getLabel(@NonNull FooterLinkEntity entity) {
        return entity.getLabel();
    }

    private Specification<FooterLinkEntity> createSearchSpecification(String query) {
        if (query == null) {
            return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.conjunction();
        }

        String likePattern = "%" + query.toLowerCase() + "%";

        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.or(
            criteriaBuilder.like(criteriaBuilder.lower(root.get("label")), likePattern),
            criteriaBuilder.like(criteriaBuilder.lower(criteriaBuilder.coalesce(root.get("url"), "")), likePattern),
            criteriaBuilder.like(criteriaBuilder.lower(criteriaBuilder.coalesce(root.get("icon"), "")), likePattern)
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
