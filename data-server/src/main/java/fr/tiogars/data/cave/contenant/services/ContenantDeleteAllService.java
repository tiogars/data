package fr.tiogars.data.cave.contenant.services;

import org.springframework.stereotype.Service;
import fr.tiogars.data.cave.contenant.repositories.ContenantRepository;

@Service
public class ContenantDeleteAllService { private final ContenantRepository contenantRepository; public ContenantDeleteAllService(ContenantRepository contenantRepository) { this.contenantRepository = contenantRepository; } public void deleteAllContenants() { contenantRepository.deleteAllInBatch(); } }
