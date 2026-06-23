package fr.tiogars.data.cave.appellation.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import fr.tiogars.data.cave.appellation.entities.AppellationEntity;
import fr.tiogars.data.cave.appellation.models.Appellation;
import fr.tiogars.data.cave.appellation.models.AppellationSearchResponse;
import fr.tiogars.data.cave.appellation.repositories.AppellationRepository;

@Service
public class AppellationSearchService {
    private final AppellationRepository appellationRepository;
    public AppellationSearchService(AppellationRepository appellationRepository) { this.appellationRepository = appellationRepository; }
    public AppellationSearchResponse searchAppellations(int page, int size, String query) {
        String q = query == null || query.isBlank() ? null : query.trim();
        Page<AppellationEntity> result = appellationRepository.findAll(createSpec(q), PageRequest.of(page, size, Sort.by(Sort.Order.asc("name"))));
        List<Appellation> items = result.getContent().stream().map(AppellationModelMapper::toModel).toList();
        return new AppellationSearchResponse(items, result.getTotalElements() > Integer.MAX_VALUE ? Integer.MAX_VALUE : (int) result.getTotalElements(), page, size, q);
    }
    private Specification<AppellationEntity> createSpec(String query) {
        if (query == null) return (root, cq, cb) -> cb.conjunction();
        String likePattern = "%" + query.toLowerCase() + "%";
        return (root, cq, cb) -> cb.like(cb.lower(root.get("name")), likePattern);
    }
}
