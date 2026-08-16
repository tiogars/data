package fr.tiogars.data.products.brand.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.core.TypedPropertyPath;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.jspecify.annotations.NonNull;

import fr.tiogars.data.products.brand.entities.BrandEntity;
import fr.tiogars.data.products.brand.models.Brand;
import fr.tiogars.data.products.brand.models.BrandSearchResponse;
import fr.tiogars.data.products.brand.repositories.BrandRepository;

@Service
public class BrandSearchService {

    private final BrandRepository brandRepository;

    public BrandSearchService(BrandRepository brandRepository) {
        this.brandRepository = brandRepository;
    }

    public BrandSearchResponse searchBrands(int page, int size, String query) {
        String normalizedQuery = normalizeQuery(query);

        Pageable pageable = PageRequest.of(
            page,
            size,
            Sort.by(TypedPropertyPath.of(BrandSearchService::getBrandName)).ascending()
        );

        Page<BrandEntity> result = brandRepository.findAll(createSearchSpecification(normalizedQuery), pageable);

        List<Brand> items = result.getContent().stream()
            .map(BrandModelMapper::toModel)
            .toList();

        return new BrandSearchResponse(items, toSafeCount(result.getTotalElements()), page, size, normalizedQuery);
    }

    private static String getBrandName(@NonNull BrandEntity entity) {
        return entity.getName();
    }

    private Specification<BrandEntity> createSearchSpecification(String query) {
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
