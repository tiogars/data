package fr.tiogars.data.cave.maison.services;

import org.springframework.stereotype.Service;
import fr.tiogars.data.common.exceptions.DataNotFoundException;
import fr.tiogars.data.cave.maison.repositories.MaisonRepository;

@Service
public class MaisonDeleteOneService { private final MaisonRepository maisonRepository; public MaisonDeleteOneService(MaisonRepository maisonRepository) { this.maisonRepository = maisonRepository; } public void deleteMaison(String id) { if (!maisonRepository.existsById(id)) throw new DataNotFoundException("Maison non trouvee pour l'id: " + id); maisonRepository.deleteById(id); } }
