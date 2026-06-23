package fr.tiogars.data.cave.vintag.services;

import java.time.OffsetDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import fr.tiogars.data.cave.vintag.models.VinTag;
import fr.tiogars.data.cave.vintag.models.VinTagPrintResponse;
import fr.tiogars.data.cave.vintag.repositories.VinTagRepository;

@Service
public class VinTagPrintService {
    private final VinTagRepository vinTagRepository;
    public VinTagPrintService(VinTagRepository vinTagRepository) { this.vinTagRepository = vinTagRepository; }
    public VinTagPrintResponse printVinTags(String mode, String nameFilter) {
        String effectiveMode = mode == null || mode.isBlank() ? "filtered" : mode.trim().toLowerCase();
        if (!"filtered".equals(effectiveMode) && !"all".equals(effectiveMode)) throw new IllegalArgumentException("Le mode d'impression doit etre 'filtered' ou 'all'.");
        List<VinTag> allItems = vinTagRepository.findAllByOrderByNameAsc().stream().map(VinTagModelMapper::toModel).toList();
        String filter = nameFilter == null || nameFilter.isBlank() ? null : nameFilter.trim();
        List<VinTag> filtered = "all".equals(effectiveMode) ? allItems : allItems.stream().filter(item -> filter == null || (item.getName() != null && item.getName().toLowerCase().contains(filter.toLowerCase()))).toList();
        return new VinTagPrintResponse(filtered, filtered.size(), OffsetDateTime.now().toString(), allItems.size());
    }
}
