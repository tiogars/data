package fr.tiogars.data.cave.appellation.services;

import org.springframework.stereotype.Service;
import fr.tiogars.data.cave.appellation.repositories.AppellationRepository;

@Service
public class AppellationDeleteOneService {
    private final AppellationRepository appellationRepository;
    public AppellationDeleteOneService(AppellationRepository appellationRepository) { this.appellationRepository = appellationRepository; }
    public void deleteAppellation(String id) { if (!appellationRepository.existsById(id)) throw new fr.tiogars.data.common.exceptions.DataNotFoundException("Appellation non trouvee pour l'id: " + id); appellationRepository.deleteById(id); }
}
