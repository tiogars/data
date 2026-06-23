package fr.tiogars.data.cave.vintag.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import fr.tiogars.data.cave.vintag.entities.VinTagEntity;
import fr.tiogars.data.cave.vintag.models.VinTag;
import fr.tiogars.data.cave.vintag.models.VinTagSearchResponse;
import fr.tiogars.data.cave.vintag.repositories.VinTagRepository;

@Service
public class VinTagSearchService {
    private final VinTagRepository vinTagRepository;
    public VinTagSearchService(VinTagRepository vinTagRepository) { this.vinTagRepository = vinTagRepository; }
    public VinTagSearchResponse searchVinTags(int page, int size, String query) {
        String q = query == null || query.isBlank() ? null : query.trim();
        Page<VinTagEntity> result = vinTagRepository.findAll(createSpec(q), PageRequest.of(page, size, Sort.by(Sort.Order.asc("name"))));
        List<VinTag> items = result.getContent().stream().map(VinTagModelMapper::toModel).toList();
        return new VinTagSearchResponse(items, result.getTotalElements() > Integer.MAX_VALUE ? Integer.MAX_VALUE : (int) result.getTotalElements(), page, size, q);
    }
    private Specification<VinTagEntity> createSpec(String query) {
        if (query == null) return (root, cq, cb) -> cb.conjunction();
        String likePattern = "%" + query.toLowerCase() + "%";
        return (root, cq, cb) -> cb.like(cb.lower(root.get("name")), likePattern);
    }
}
