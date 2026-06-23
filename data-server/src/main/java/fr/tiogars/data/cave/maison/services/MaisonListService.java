package fr.tiogars.data.cave.maison.services;

import org.springframework.stereotype.Service;
import fr.tiogars.data.cave.maison.models.MaisonListResponse;
import fr.tiogars.data.cave.maison.repositories.MaisonRepository;

@Service
public class MaisonListService { private final MaisonRepository maisonRepository; public MaisonListService(MaisonRepository maisonRepository) { this.maisonRepository = maisonRepository; } public MaisonListResponse listMaisons() { var entities = maisonRepository.findAllByOrderByNameAsc(); return new MaisonListResponse(entities.stream().map(MaisonModelMapper::toModel).toList(), entities.size()); } }
