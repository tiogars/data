package fr.tiogars.data.cave.vin.services;

import java.time.OffsetDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import fr.tiogars.data.cave.vin.models.Vin;
import fr.tiogars.data.cave.vin.models.VinPrintResponse;

@Service
public class VinPrintService {

    private final VinListService vinListService;

    public VinPrintService(VinListService vinListService) {
        this.vinListService = vinListService;
    }

    public VinPrintResponse printVins(String mode, Integer annee, String region) {
        String effectiveMode = mode == null || mode.isBlank() ? "filtered" : mode.trim().toLowerCase();
        if (!"filtered".equals(effectiveMode) && !"all".equals(effectiveMode)) {
            throw new IllegalArgumentException("Le mode d'impression doit être 'filtered' ou 'all'.");
        }

        List<Vin> allItems = vinListService.listVins().getItems();
        List<Vin> filteredItems = "all".equals(effectiveMode)
            ? allItems
            : allItems.stream()
                .filter(item -> annee == null || annee.equals(item.getAnnee()))
                .filter(item -> containsIgnoreCase(item.getRegion(), region))
                .toList();

        return new VinPrintResponse(filteredItems, filteredItems.size(), OffsetDateTime.now().toString(), allItems.size());
    }

    private static boolean containsIgnoreCase(String candidate, String filter) {
        if (filter == null || filter.isBlank()) {
            return true;
        }
        if (candidate == null) {
            return false;
        }
        return candidate.toLowerCase().contains(filter.trim().toLowerCase());
    }
}
