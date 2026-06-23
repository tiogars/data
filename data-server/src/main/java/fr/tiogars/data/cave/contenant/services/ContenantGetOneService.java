package fr.tiogars.data.cave.contenant.services;

import org.springframework.stereotype.Service;
import fr.tiogars.data.common.exceptions.DataNotFoundException;
import fr.tiogars.data.cave.contenant.models.Contenant;
import fr.tiogars.data.cave.contenant.repositories.ContenantRepository;

@Service
public class ContenantGetOneService { private final ContenantRepository contenantRepository; public ContenantGetOneService(ContenantRepository contenantRepository) { this.contenantRepository = contenantRepository; } public Contenant getContenant(String id) { return contenantRepository.findById(id).map(ContenantModelMapper::toModel).orElseThrow(() -> new DataNotFoundException("Contenant non trouve pour l'id: " + id)); } }
