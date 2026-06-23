package fr.tiogars.data.cave.circonstance.services;

import org.springframework.stereotype.Service;

import fr.tiogars.data.common.exceptions.DataNotFoundException;
import fr.tiogars.data.cave.circonstance.entities.CirconstanceEntity;
import fr.tiogars.data.cave.circonstance.models.Circonstance;
import fr.tiogars.data.cave.circonstance.repositories.CirconstanceRepository;

@Service
public class CirconstanceUpdateService {
    private final CirconstanceRepository circonstanceRepository;
    private final CirconstanceCreationService circonstanceCreationService;
    public CirconstanceUpdateService(CirconstanceRepository circonstanceRepository, CirconstanceCreationService circonstanceCreationService) { this.circonstanceRepository = circonstanceRepository; this.circonstanceCreationService = circonstanceCreationService; }
    public Circonstance updateCirconstance(String id, Circonstance circonstance) {
        CirconstanceEntity entity = circonstanceRepository.findById(id).orElseThrow(() -> new DataNotFoundException("Circonstance non trouvee pour l'id: " + id));
        circonstanceCreationService.validateUniqueName(circonstance.getName(), id);
        CirconstanceCreationService.applyValues(entity, circonstance.getName());
        return CirconstanceModelMapper.toModel(circonstanceRepository.save(entity));
    }
}
