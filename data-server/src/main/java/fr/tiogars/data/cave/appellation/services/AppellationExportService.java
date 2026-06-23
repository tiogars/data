package fr.tiogars.data.cave.appellation.services;

import org.springframework.stereotype.Service;
import fr.tiogars.data.cave.appellation.repositories.AppellationRepository;
import fr.tiogars.data.cave.appellation.models.AppellationListResponse;

@Service
public class AppellationExportService {
    private final AppellationRepository appellationRepository;
    public AppellationExportService(AppellationRepository appellationRepository) { this.appellationRepository = appellationRepository; }
    public AppellationListResponse exportAppellations() { var items = appellationRepository.findAllByOrderByNameAsc().stream().map(AppellationModelMapper::toModel).toList(); return new AppellationListResponse(items, items.size()); }
}
