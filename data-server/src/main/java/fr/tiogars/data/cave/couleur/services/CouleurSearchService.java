package fr.tiogars.data.cave.couleur.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.core.TypedPropertyPath;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.jspecify.annotations.NonNull;

import fr.tiogars.data.cave.couleur.entities.CouleurEntity;
import fr.tiogars.data.cave.couleur.models.Couleur;
import fr.tiogars.data.cave.couleur.models.CouleurSearchResponse;
import fr.tiogars.data.cave.couleur.repositories.CouleurRepository;

@Service
public class CouleurSearchService {
    private final CouleurRepository couleurRepository;
    public CouleurSearchService(CouleurRepository couleurRepository) { this.couleurRepository = couleurRepository; }
    public CouleurSearchResponse searchCouleurs(int page, int size, String query) {
        String q = query == null || query.isBlank() ? null : query.trim();
        Page<CouleurEntity> result = couleurRepository.findAll(createSpec(q), PageRequest.of(page, size, Sort.by(TypedPropertyPath.of(CouleurSearchService::getCouleurName)).ascending()));
        List<Couleur> items = result.getContent().stream().map(CouleurModelMapper::toModel).toList();
        return new CouleurSearchResponse(items, result.getTotalElements() > Integer.MAX_VALUE ? Integer.MAX_VALUE : (int) result.getTotalElements(), page, size, q);
    }
    private static String getCouleurName(@NonNull CouleurEntity entity) {
        return entity.getName();
    }
    private Specification<CouleurEntity> createSpec(String query) {
        if (query == null) return (root, cq, cb) -> cb.conjunction();
        String likePattern = "%" + query.toLowerCase() + "%";
        return (root, cq, cb) -> cb.like(cb.lower(root.get("name")), likePattern);
    }
}
