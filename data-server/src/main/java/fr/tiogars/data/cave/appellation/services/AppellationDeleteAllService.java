package fr.tiogars.data.cave.appellation.services;

import org.springframework.stereotype.Service;
import fr.tiogars.data.cave.appellation.repositories.AppellationRepository;

@Service
public class AppellationDeleteAllService {
    private final AppellationRepository appellationRepository;
    public AppellationDeleteAllService(AppellationRepository appellationRepository) { this.appellationRepository = appellationRepository; }
    public void deleteAllAppellations() { appellationRepository.deleteAllInBatch(); }
}
