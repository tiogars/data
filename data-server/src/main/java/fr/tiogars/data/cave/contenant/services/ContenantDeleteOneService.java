package fr.tiogars.data.cave.contenant.services;

import org.springframework.stereotype.Service;
import fr.tiogars.data.common.exceptions.DataNotFoundException;
import fr.tiogars.data.cave.contenant.repositories.ContenantRepository;

@Service
public class ContenantDeleteOneService { private final ContenantRepository contenantRepository; public ContenantDeleteOneService(ContenantRepository contenantRepository) { this.contenantRepository = contenantRepository; } public void deleteContenant(String id) { if (!contenantRepository.existsById(id)) throw new DataNotFoundException("Contenant non trouve pour l'id: " + id); contenantRepository.deleteById(id); } }
