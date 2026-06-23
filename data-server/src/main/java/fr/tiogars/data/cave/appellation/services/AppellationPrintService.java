package fr.tiogars.data.cave.appellation.services;

import java.time.OffsetDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import fr.tiogars.data.cave.appellation.models.Appellation;
import fr.tiogars.data.cave.appellation.models.AppellationPrintResponse;
import fr.tiogars.data.cave.appellation.repositories.AppellationRepository;

@Service
public class AppellationPrintService {
    private final AppellationRepository appellationRepository;
    public AppellationPrintService(AppellationRepository appellationRepository) { this.appellationRepository = appellationRepository; }
    public AppellationPrintResponse printAppellations(String mode, String nameFilter) {
        String effectiveMode = mode == null || mode.isBlank() ? "filtered" : mode.trim().toLowerCase();
        if (!"filtered".equals(effectiveMode) && !"all".equals(effectiveMode)) throw new IllegalArgumentException("Le mode d'impression doit etre 'filtered' ou 'all'.");
        List<Appellation> allItems = appellationRepository.findAllByOrderByNameAsc().stream().map(AppellationModelMapper::toModel).toList();
        String filter = nameFilter == null || nameFilter.isBlank() ? null : nameFilter.trim();
        List<Appellation> filtered = "all".equals(effectiveMode) ? allItems : allItems.stream().filter(item -> filter == null || (item.getName() != null && item.getName().toLowerCase().contains(filter.toLowerCase()))).toList();
        return new AppellationPrintResponse(filtered, filtered.size(), OffsetDateTime.now().toString(), allItems.size());
    }
}
