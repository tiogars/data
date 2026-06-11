package fr.tiogars.data.products.brand.services;

import java.time.OffsetDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import fr.tiogars.data.products.brand.models.Brand;
import fr.tiogars.data.products.brand.models.BrandPrintResponse;
import fr.tiogars.data.products.brand.repositories.BrandRepository;

@Service
public class BrandPrintService {

    private final BrandRepository brandRepository;

    public BrandPrintService(BrandRepository brandRepository) {
        this.brandRepository = brandRepository;
    }

    public BrandPrintResponse printBrands(String mode, String nameFilter, String descriptionFilter) {
        String effectiveMode = mode == null || mode.isBlank() ? "filtered" : mode.trim().toLowerCase();
        if (!"filtered".equals(effectiveMode) && !"all".equals(effectiveMode)) {
            throw new IllegalArgumentException("Le mode d'impression doit etre 'filtered' ou 'all'.");
        }

        List<Brand> allItems = brandRepository.findAllByOrderByNameAsc().stream()
            .map(BrandModelMapper::toModel)
            .toList();

        String normalizedNameFilter = normalizeFilter(nameFilter);
        String normalizedDescriptionFilter = normalizeFilter(descriptionFilter);

        List<Brand> filteredItems = "all".equals(effectiveMode)
            ? allItems
            : allItems.stream()
                .filter(item -> containsIgnoreCase(item.getName(), normalizedNameFilter))
                .filter(item -> containsIgnoreCase(item.getDescription(), normalizedDescriptionFilter))
                .toList();

        return new BrandPrintResponse(filteredItems, filteredItems.size(), OffsetDateTime.now().toString(), allItems.size());
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
