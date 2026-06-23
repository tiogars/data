package fr.tiogars.data.cave.circonstance.services;

import org.springframework.stereotype.Service;
import fr.tiogars.data.cave.circonstance.repositories.CirconstanceRepository;

@Service
public class CirconstanceDeleteOneService {
    private final CirconstanceRepository circonstanceRepository;
    public CirconstanceDeleteOneService(CirconstanceRepository circonstanceRepository) { this.circonstanceRepository = circonstanceRepository; }
    public void deleteCirconstance(String id) { if (!circonstanceRepository.existsById(id)) throw new fr.tiogars.data.common.exceptions.DataNotFoundException("Circonstance non trouvee pour l'id: " + id); circonstanceRepository.deleteById(id); }
}
