package fr.tiogars.data.cave.vintag.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.core.TypedPropertyPath;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.jspecify.annotations.NonNull;

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
        Page<VinTagEntity> result = vinTagRepository.findAll(createSpec(q), PageRequest.of(page, size, Sort.by(TypedPropertyPath.of(VinTagSearchService::getVinTagName)).ascending()));
        List<VinTag> items = result.getContent().stream().map(VinTagModelMapper::toModel).toList();
        return new VinTagSearchResponse(items, result.getTotalElements() > Integer.MAX_VALUE ? Integer.MAX_VALUE : (int) result.getTotalElements(), page, size, q);
    }
    private static String getVinTagName(@NonNull VinTagEntity entity) {
        return entity.getName();
    }
    private Specification<VinTagEntity> createSpec(String query) {
        if (query == null) return (root, cq, cb) -> cb.conjunction();
        String likePattern = "%" + query.toLowerCase() + "%";
        return (root, cq, cb) -> cb.like(cb.lower(root.get("name")), likePattern);
    }
}
