package fr.tiogars.data.cave.cepage.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import fr.tiogars.data.cave.cepage.entities.CepageEntity;
import fr.tiogars.data.cave.cepage.models.Cepage;
import fr.tiogars.data.cave.cepage.models.CepageSearchResponse;
import fr.tiogars.data.cave.cepage.repositories.CepageRepository;

@Service
public class CepageSearchService {
    private final CepageRepository cepageRepository;
    public CepageSearchService(CepageRepository cepageRepository) { this.cepageRepository = cepageRepository; }
    public CepageSearchResponse searchCepages(int page, int size, String query) {
        String q = query == null || query.isBlank() ? null : query.trim();
        Page<CepageEntity> result = cepageRepository.findAll(createSpec(q), PageRequest.of(page, size, Sort.by(Sort.Order.asc("name"))));
        List<Cepage> items = result.getContent().stream().map(CepageModelMapper::toModel).toList();
        return new CepageSearchResponse(items, result.getTotalElements() > Integer.MAX_VALUE ? Integer.MAX_VALUE : (int) result.getTotalElements(), page, size, q);
    }
    private Specification<CepageEntity> createSpec(String query) {
        if (query == null) return (root, cq, cb) -> cb.conjunction();
        String likePattern = "%" + query.toLowerCase() + "%";
        return (root, cq, cb) -> cb.like(cb.lower(root.get("name")), likePattern);
    }
}
