package fr.tiogars.data.cave.appellation.services;

import org.springframework.stereotype.Service;

import fr.tiogars.data.common.exceptions.DataNotFoundException;
import fr.tiogars.data.cave.appellation.entities.AppellationEntity;
import fr.tiogars.data.cave.appellation.models.Appellation;
import fr.tiogars.data.cave.appellation.repositories.AppellationRepository;

@Service
public class AppellationUpdateService {
    private final AppellationRepository appellationRepository;
    private final AppellationCreationService appellationCreationService;
    public AppellationUpdateService(AppellationRepository appellationRepository, AppellationCreationService appellationCreationService) { this.appellationRepository = appellationRepository; this.appellationCreationService = appellationCreationService; }
    public Appellation updateAppellation(String id, Appellation appellation) {
        AppellationEntity entity = appellationRepository.findById(id).orElseThrow(() -> new DataNotFoundException("Appellation non trouvee pour l'id: " + id));
        appellationCreationService.validateUniqueName(appellation.getName(), id);
        AppellationCreationService.applyValues(entity, appellation.getName());
        return AppellationModelMapper.toModel(appellationRepository.save(entity));
    }
}
