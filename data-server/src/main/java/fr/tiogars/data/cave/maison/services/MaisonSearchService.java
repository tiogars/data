package fr.tiogars.data.cave.maison.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.core.TypedPropertyPath;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.jspecify.annotations.NonNull;

import fr.tiogars.data.cave.maison.entities.MaisonEntity;
import fr.tiogars.data.cave.maison.models.Maison;
import fr.tiogars.data.cave.maison.models.MaisonSearchResponse;
import fr.tiogars.data.cave.maison.repositories.MaisonRepository;

@Service
public class MaisonSearchService {
	private final MaisonRepository maisonRepository;

	public MaisonSearchService(MaisonRepository maisonRepository) {
		this.maisonRepository = maisonRepository;
	}

	public MaisonSearchResponse searchMaisons(int page, int size, String query) {
		String q = query == null || query.isBlank() ? null : query.trim();
		Page<MaisonEntity> result = maisonRepository.findAll(createSpec(q), PageRequest.of(page, size, Sort.by(TypedPropertyPath.of(MaisonSearchService::getMaisonName)).ascending()));
		List<Maison> items = result.getContent().stream().map(MaisonModelMapper::toModel).toList();
		return new MaisonSearchResponse(items, result.getTotalElements() > Integer.MAX_VALUE ? Integer.MAX_VALUE : (int) result.getTotalElements(), page, size, q);
	}

	private static String getMaisonName(@NonNull MaisonEntity entity) {
		return entity.getName();
	}

	private Specification<MaisonEntity> createSpec(String query) {
		if (query == null) return (root, cq, cb) -> cb.conjunction();
		String likePattern = "%" + query.toLowerCase() + "%";
		return (root, cq, cb) -> cb.or(cb.like(cb.lower(root.get("name")), likePattern), cb.like(cb.lower(cb.coalesce(root.get("website"), "")), likePattern));
	}
}
