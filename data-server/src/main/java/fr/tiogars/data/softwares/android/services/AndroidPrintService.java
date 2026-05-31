package fr.tiogars.data.softwares.android.services;

import java.time.OffsetDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import fr.tiogars.data.softwares.android.models.Android;
import fr.tiogars.data.softwares.android.models.AndroidPrintResponse;
import fr.tiogars.data.softwares.android.repositories.AndroidRepository;

@Service
public class AndroidPrintService {

    private final AndroidRepository androidRepository;

    public AndroidPrintService(AndroidRepository androidRepository) {
        this.androidRepository = androidRepository;
    }

    public AndroidPrintResponse printAndroids(String mode, String nameFilter, String packageNameFilter, String categoryFilter, String descriptionFilter) {
        String effectiveMode = mode == null || mode.isBlank() ? "filtered" : mode.trim().toLowerCase();
        if (!"filtered".equals(effectiveMode) && !"all".equals(effectiveMode)) {
            throw new IllegalArgumentException("Le mode d'impression doit etre 'filtered' ou 'all'.");
        }

        List<Android> allItems = androidRepository.findAllByOrderByNameAsc().stream()
            .map(AndroidModelMapper::toModel)
            .toList();

        String normalizedNameFilter = normalizeFilter(nameFilter);
        String normalizedPackageNameFilter = normalizeFilter(packageNameFilter);
        String normalizedCategoryFilter = normalizeFilter(categoryFilter);
        String normalizedDescriptionFilter = normalizeFilter(descriptionFilter);

        List<Android> filteredItems = "all".equals(effectiveMode)
            ? allItems
            : allItems.stream()
                .filter(item -> containsIgnoreCase(item.getName(), normalizedNameFilter))
                .filter(item -> containsIgnoreCase(item.getPackageName(), normalizedPackageNameFilter))
                .filter(item -> containsCategoryIgnoreCase(item.getCategory(), normalizedCategoryFilter))
                .filter(item -> containsIgnoreCase(item.getDescription(), normalizedDescriptionFilter))
                .toList();

        return new AndroidPrintResponse(filteredItems, filteredItems.size(), OffsetDateTime.now().toString(), allItems.size());
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

    private static boolean containsCategoryIgnoreCase(List<String> categories, String filter) {
        if (filter == null) {
            return true;
        }
        if (categories == null || categories.isEmpty()) {
            return false;
        }
        for (String category : categories) {
            if (containsIgnoreCase(category, filter)) {
                return true;
            }
        }
        return false;
    }
}