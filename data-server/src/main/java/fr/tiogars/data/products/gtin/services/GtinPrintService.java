package fr.tiogars.data.products.gtin.services;

import java.time.OffsetDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import fr.tiogars.data.products.gtin.models.Gtin;
import fr.tiogars.data.products.gtin.models.GtinPrintResponse;
import fr.tiogars.data.products.gtin.repositories.GtinRepository;

@Service
public class GtinPrintService {

    private final GtinRepository gtinRepository;

    public GtinPrintService(GtinRepository gtinRepository) {
        this.gtinRepository = gtinRepository;
    }

    public GtinPrintResponse printGtins(String mode, String codeFilter, String descriptionFilter) {
        String effectiveMode = mode == null || mode.isBlank() ? "filtered" : mode.trim().toLowerCase();
        if (!"filtered".equals(effectiveMode) && !"all".equals(effectiveMode)) {
            throw new IllegalArgumentException("Le mode d'impression doit etre 'filtered' ou 'all'.");
        }

        List<Gtin> allItems = gtinRepository.findAllByOrderByCodeAsc().stream()
            .map(GtinModelMapper::toModel)
            .toList();

        String normalizedCodeFilter = normalizeFilter(codeFilter);
        String normalizedDescriptionFilter = normalizeFilter(descriptionFilter);

        List<Gtin> filteredItems = "all".equals(effectiveMode)
            ? allItems
            : allItems.stream()
                .filter(item -> containsIgnoreCase(item.getCode(), normalizedCodeFilter))
                .filter(item -> containsIgnoreCase(item.getDescription(), normalizedDescriptionFilter))
                .toList();

        return new GtinPrintResponse(filteredItems, filteredItems.size(), OffsetDateTime.now().toString(), allItems.size());
    }

    private static String normalizeFilter(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return value.trim();
    }

    private static boolean containsIgnoreCase(String candidate, String filter) {
        if (filter == null) {
            return true;
        }
        if (candidate == null) {
            return false;
        }
        return candidate.toLowerCase().contains(filter.toLowerCase());
    }
}
