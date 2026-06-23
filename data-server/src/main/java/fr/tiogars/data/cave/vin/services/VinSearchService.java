package fr.tiogars.data.cave.vin.services;

import static fr.tiogars.data.common.validation.TextValidationUtils.normalizeNullableText;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import fr.tiogars.data.cave.vin.entities.VinEntity;
import fr.tiogars.data.cave.vin.models.Vin;
import fr.tiogars.data.cave.vin.models.VinSearchResponse;
import fr.tiogars.data.cave.vin.repositories.VinRepository;

@Service
public class VinSearchService {

    private final VinRepository vinRepository;
    private final VinListService vinListService;

    public VinSearchService(VinRepository vinRepository, VinListService vinListService) {
        this.vinRepository = vinRepository;
        this.vinListService = vinListService;
    }

    public VinSearchResponse searchVins(int page, int size, String query, String appellationId, String couleurId, Integer annee) {
        String normalizedQuery = normalizeQuery(query);
        String normalizedAppellationId = normalizeNullableText(appellationId);
        String normalizedCouleurId = normalizeNullableText(couleurId);

        Pageable pageable = PageRequest.of(
            page,
            size,
            Sort.by(Sort.Order.desc("createdAt"))
        );

        Page<VinEntity> result = vinRepository.findAll(
            createSearchSpecification(normalizedQuery, normalizedAppellationId, normalizedCouleurId, annee),
            pageable
        );

        List<Vin> items = vinListService.mapEntities(result.getContent());
        return new VinSearchResponse(items, toSafeCount(result.getTotalElements()), page, size, normalizedQuery);
    }

    private Specification<VinEntity> createSearchSpecification(String query, String appellationId, String couleurId, Integer annee) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            var predicate = criteriaBuilder.conjunction();

            if (appellationId != null) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("appellationId"), appellationId));
            }
            if (couleurId != null) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("couleurId"), couleurId));
            }
            if (annee != null) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("annee"), annee));
            }
            if (query != null) {
                String likePattern = "%" + query.toLowerCase() + "%";
                predicate = criteriaBuilder.and(
                    predicate,
                    criteriaBuilder.or(
                        criteriaBuilder.like(criteriaBuilder.lower(criteriaBuilder.coalesce(root.get("commune"), "")), likePattern),
                        criteriaBuilder.like(criteriaBuilder.lower(criteriaBuilder.coalesce(root.get("region"), "")), likePattern),
                        criteriaBuilder.like(criteriaBuilder.lower(criteriaBuilder.coalesce(root.get("commentaires"), "")), likePattern),
                        criteriaBuilder.like(criteriaBuilder.lower(criteriaBuilder.coalesce(root.get("accordsMetsVins"), "")), likePattern)
                    )
                );
            }

            return predicate;
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
