package fr.tiogars.data.cave.circonstance.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import fr.tiogars.data.cave.circonstance.entities.CirconstanceEntity;
import fr.tiogars.data.cave.circonstance.models.Circonstance;
import fr.tiogars.data.cave.circonstance.models.CirconstanceSearchResponse;
import fr.tiogars.data.cave.circonstance.repositories.CirconstanceRepository;

@Service
public class CirconstanceSearchService {
    private final CirconstanceRepository circonstanceRepository;
    public CirconstanceSearchService(CirconstanceRepository circonstanceRepository) { this.circonstanceRepository = circonstanceRepository; }
    public CirconstanceSearchResponse searchCirconstances(int page, int size, String query) {
        String q = query == null || query.isBlank() ? null : query.trim();
        Page<CirconstanceEntity> result = circonstanceRepository.findAll(createSpec(q), PageRequest.of(page, size, Sort.by(Sort.Order.asc("name"))));
        List<Circonstance> items = result.getContent().stream().map(CirconstanceModelMapper::toModel).toList();
        return new CirconstanceSearchResponse(items, result.getTotalElements() > Integer.MAX_VALUE ? Integer.MAX_VALUE : (int) result.getTotalElements(), page, size, q);
    }
    private Specification<CirconstanceEntity> createSpec(String query) {
        if (query == null) return (root, cq, cb) -> cb.conjunction();
        String likePattern = "%" + query.toLowerCase() + "%";
        return (root, cq, cb) -> cb.like(cb.lower(root.get("name")), likePattern);
    }
}
