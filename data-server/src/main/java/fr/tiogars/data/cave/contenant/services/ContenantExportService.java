package fr.tiogars.data.cave.contenant.services;

import org.springframework.stereotype.Service;
import fr.tiogars.data.cave.contenant.models.ContenantListResponse;
import fr.tiogars.data.cave.contenant.repositories.ContenantRepository;

@Service
public class ContenantExportService { private final ContenantRepository contenantRepository; public ContenantExportService(ContenantRepository contenantRepository) { this.contenantRepository = contenantRepository; } public ContenantListResponse exportContenants() { var items = contenantRepository.findAllByOrderByNameAsc().stream().map(ContenantModelMapper::toModel).toList(); return new ContenantListResponse(items, items.size()); } }
