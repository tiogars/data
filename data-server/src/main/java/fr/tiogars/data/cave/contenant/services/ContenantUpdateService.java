package fr.tiogars.data.cave.contenant.services;

import org.springframework.stereotype.Service;

import fr.tiogars.data.common.exceptions.DataNotFoundException;
import fr.tiogars.data.cave.contenant.entities.ContenantEntity;
import fr.tiogars.data.cave.contenant.models.Contenant;
import fr.tiogars.data.cave.contenant.repositories.ContenantRepository;

@Service
public class ContenantUpdateService { private final ContenantRepository contenantRepository; private final ContenantCreationService contenantCreationService; public ContenantUpdateService(ContenantRepository contenantRepository, ContenantCreationService contenantCreationService) { this.contenantRepository = contenantRepository; this.contenantCreationService = contenantCreationService; } public Contenant updateContenant(String id, Contenant contenant) { ContenantEntity entity = contenantRepository.findById(id).orElseThrow(() -> new DataNotFoundException("Contenant non trouve pour l'id: " + id)); contenantCreationService.validateUniqueName(contenant.getName(), id); ContenantCreationService.applyValues(entity, contenant.getName(), contenant.getVolumeCl()); return ContenantModelMapper.toModel(contenantRepository.save(entity)); } }
