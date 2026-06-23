package fr.tiogars.data.cave.maison.services;

import org.springframework.stereotype.Service;
import fr.tiogars.data.cave.maison.models.MaisonListResponse;
import fr.tiogars.data.cave.maison.repositories.MaisonRepository;

@Service
public class MaisonExportService { private final MaisonRepository maisonRepository; public MaisonExportService(MaisonRepository maisonRepository) { this.maisonRepository = maisonRepository; } public MaisonListResponse exportMaisons() { var items = maisonRepository.findAllByOrderByNameAsc().stream().map(MaisonModelMapper::toModel).toList(); return new MaisonListResponse(items, items.size()); } }
