package fr.tiogars.data.cave.circonstance.services;

import java.time.OffsetDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import fr.tiogars.data.cave.circonstance.models.Circonstance;
import fr.tiogars.data.cave.circonstance.models.CirconstancePrintResponse;
import fr.tiogars.data.cave.circonstance.repositories.CirconstanceRepository;

@Service
public class CirconstancePrintService {
    private final CirconstanceRepository circonstanceRepository;
    public CirconstancePrintService(CirconstanceRepository circonstanceRepository) { this.circonstanceRepository = circonstanceRepository; }
    public CirconstancePrintResponse printCirconstances(String mode, String nameFilter) {
        String effectiveMode = mode == null || mode.isBlank() ? "filtered" : mode.trim().toLowerCase();
        if (!"filtered".equals(effectiveMode) && !"all".equals(effectiveMode)) throw new IllegalArgumentException("Le mode d'impression doit etre 'filtered' ou 'all'.");
        List<Circonstance> allItems = circonstanceRepository.findAllByOrderByNameAsc().stream().map(CirconstanceModelMapper::toModel).toList();
        String filter = nameFilter == null || nameFilter.isBlank() ? null : nameFilter.trim();
        List<Circonstance> filtered = "all".equals(effectiveMode) ? allItems : allItems.stream().filter(item -> filter == null || (item.getName() != null && item.getName().toLowerCase().contains(filter.toLowerCase()))).toList();
        return new CirconstancePrintResponse(filtered, filtered.size(), OffsetDateTime.now().toString(), allItems.size());
    }
}
