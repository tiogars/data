package fr.tiogars.data.cave.circonstance.services;

import org.springframework.stereotype.Service;
import fr.tiogars.data.cave.circonstance.repositories.CirconstanceRepository;
import fr.tiogars.data.cave.circonstance.models.Circonstance;

@Service
public class CirconstanceGetOneService {
    private final CirconstanceRepository circonstanceRepository;
    public CirconstanceGetOneService(CirconstanceRepository circonstanceRepository) { this.circonstanceRepository = circonstanceRepository; }
    public Circonstance getCirconstance(String id) { return circonstanceRepository.findById(id).map(CirconstanceModelMapper::toModel).orElseThrow(() -> new fr.tiogars.data.common.exceptions.DataNotFoundException("Circonstance non trouvee pour l'id: " + id)); }
}
