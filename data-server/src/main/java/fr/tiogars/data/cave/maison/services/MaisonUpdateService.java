package fr.tiogars.data.cave.maison.services;

import org.springframework.stereotype.Service;

import fr.tiogars.data.common.exceptions.DataNotFoundException;
import fr.tiogars.data.cave.maison.entities.MaisonEntity;
import fr.tiogars.data.cave.maison.models.Maison;
import fr.tiogars.data.cave.maison.repositories.MaisonRepository;

@Service
public class MaisonUpdateService { private final MaisonRepository maisonRepository; private final MaisonCreationService maisonCreationService; public MaisonUpdateService(MaisonRepository maisonRepository, MaisonCreationService maisonCreationService) { this.maisonRepository = maisonRepository; this.maisonCreationService = maisonCreationService; } public Maison updateMaison(String id, Maison maison) { MaisonEntity entity = maisonRepository.findById(id).orElseThrow(() -> new DataNotFoundException("Maison non trouvee pour l'id: " + id)); maisonCreationService.validateUniqueName(maison.getName(), id); MaisonCreationService.applyValues(entity, maison.getName(), maison.getWebsite()); return MaisonModelMapper.toModel(maisonRepository.save(entity)); } }
