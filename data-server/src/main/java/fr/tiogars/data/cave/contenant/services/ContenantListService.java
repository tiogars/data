package fr.tiogars.data.cave.contenant.services;

import org.springframework.stereotype.Service;
import fr.tiogars.data.cave.contenant.models.ContenantListResponse;
import fr.tiogars.data.cave.contenant.repositories.ContenantRepository;

@Service
public class ContenantListService { private final ContenantRepository contenantRepository; public ContenantListService(ContenantRepository contenantRepository) { this.contenantRepository = contenantRepository; } public ContenantListResponse listContenants() { var entities = contenantRepository.findAllByOrderByNameAsc(); return new ContenantListResponse(entities.stream().map(ContenantModelMapper::toModel).toList(), entities.size()); } }
