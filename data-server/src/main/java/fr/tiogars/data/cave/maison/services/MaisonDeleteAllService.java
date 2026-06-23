package fr.tiogars.data.cave.maison.services;

import org.springframework.stereotype.Service;
import fr.tiogars.data.cave.maison.repositories.MaisonRepository;

@Service
public class MaisonDeleteAllService { private final MaisonRepository maisonRepository; public MaisonDeleteAllService(MaisonRepository maisonRepository) { this.maisonRepository = maisonRepository; } public void deleteAllMaisons() { maisonRepository.deleteAllInBatch(); } }
