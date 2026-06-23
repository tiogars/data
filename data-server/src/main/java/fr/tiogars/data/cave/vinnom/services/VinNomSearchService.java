package fr.tiogars.data.cave.vinnom.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import fr.tiogars.data.cave.maison.repositories.MaisonRepository;
import fr.tiogars.data.cave.vinnom.entities.VinNomEntity;
import fr.tiogars.data.cave.vinnom.models.VinNom;
import fr.tiogars.data.cave.vinnom.models.VinNomSearchResponse;
import fr.tiogars.data.cave.vinnom.repositories.VinNomRepository;

@Service
public class VinNomSearchService {
    private final VinNomRepository vinNomRepository;
    private final MaisonRepository maisonRepository;
    public VinNomSearchService(VinNomRepository vinNomRepository, MaisonRepository maisonRepository) { this.vinNomRepository = vinNomRepository; this.maisonRepository = maisonRepository; }
    public VinNomSearchResponse searchVinNoms(int page, int size, String query) {
        String q = query == null || query.isBlank() ? null : query.trim();
        Page<VinNomEntity> result = vinNomRepository.findAll(createSpec(q), PageRequest.of(page, size, Sort.by(Sort.Order.asc("name"))));
        List<VinNom> items = VinNomModelMapper.toModels(result.getContent(), maisonRepository);
        return new VinNomSearchResponse(items, result.getTotalElements() > Integer.MAX_VALUE ? Integer.MAX_VALUE : (int) result.getTotalElements(), page, size, q);
    }
    private Specification<VinNomEntity> createSpec(String query) {
        if (query == null) return (root, cq, cb) -> cb.conjunction();
        String likePattern = "%" + query.toLowerCase() + "%";
        return (root, cq, cb) -> cb.or(cb.like(cb.lower(root.get("name")), likePattern), cb.like(cb.lower(cb.coalesce(root.get("maisonId"), "")), likePattern));
    }
}
