package fr.tiogars.data.cave.appellation.services;

import org.springframework.stereotype.Service;
import fr.tiogars.data.cave.appellation.repositories.AppellationRepository;
import fr.tiogars.data.cave.appellation.models.AppellationListResponse;

@Service
public class AppellationListService {
    private final AppellationRepository appellationRepository;
    public AppellationListService(AppellationRepository appellationRepository) { this.appellationRepository = appellationRepository; }
    public AppellationListResponse listAppellations() { var entities = appellationRepository.findAllByOrderByNameAsc(); return new AppellationListResponse(entities.stream().map(AppellationModelMapper::toModel).toList(), entities.size()); }
}
