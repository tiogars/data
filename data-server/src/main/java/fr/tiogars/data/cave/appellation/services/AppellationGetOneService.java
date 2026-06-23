package fr.tiogars.data.cave.appellation.services;

import org.springframework.stereotype.Service;
import fr.tiogars.data.cave.appellation.repositories.AppellationRepository;
import fr.tiogars.data.cave.appellation.models.Appellation;

@Service
public class AppellationGetOneService {
    private final AppellationRepository appellationRepository;
    public AppellationGetOneService(AppellationRepository appellationRepository) { this.appellationRepository = appellationRepository; }
    public Appellation getAppellation(String id) { return appellationRepository.findById(id).map(AppellationModelMapper::toModel).orElseThrow(() -> new fr.tiogars.data.common.exceptions.DataNotFoundException("Appellation non trouvee pour l'id: " + id)); }
}
