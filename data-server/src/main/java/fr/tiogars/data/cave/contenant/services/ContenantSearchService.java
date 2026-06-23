package fr.tiogars.data.cave.contenant.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import fr.tiogars.data.cave.contenant.entities.ContenantEntity;
import fr.tiogars.data.cave.contenant.models.Contenant;
import fr.tiogars.data.cave.contenant.models.ContenantSearchResponse;
import fr.tiogars.data.cave.contenant.repositories.ContenantRepository;

@Service
public class ContenantSearchService { private final ContenantRepository contenantRepository; public ContenantSearchService(ContenantRepository contenantRepository) { this.contenantRepository = contenantRepository; } public ContenantSearchResponse searchContenants(int page, int size, String query) { String q = query == null || query.isBlank() ? null : query.trim(); Page<ContenantEntity> result = contenantRepository.findAll(createSpec(q), PageRequest.of(page, size, Sort.by(Sort.Order.asc("name")))); List<Contenant> items = result.getContent().stream().map(ContenantModelMapper::toModel).toList(); return new ContenantSearchResponse(items, result.getTotalElements() > Integer.MAX_VALUE ? Integer.MAX_VALUE : (int) result.getTotalElements(), page, size, q); } private Specification<ContenantEntity> createSpec(String query) { if (query == null) return (root, cq, cb) -> cb.conjunction(); String likePattern = "%" + query.toLowerCase() + "%"; return (root, cq, cb) -> cb.like(cb.lower(root.get("name")), likePattern); } }
