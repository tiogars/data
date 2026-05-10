package fr.tiogars.data.dev.docs.model.services;

import java.time.OffsetDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import fr.tiogars.data.dev.docs.model.models.Model;
import fr.tiogars.data.dev.docs.model.models.ModelPrintResponse;
import fr.tiogars.data.dev.docs.model.repositories.ModelRepository;

@Service
public class ModelPrintService {

    private final ModelRepository modelRepository;

    public ModelPrintService(ModelRepository modelRepository) {
        this.modelRepository = modelRepository;
    }

    public ModelPrintResponse printModels(String mode, String nameFilter, String descriptionFilter) {
        String effectiveMode = mode == null || mode.isBlank() ? "filtered" : mode.trim().toLowerCase();
        if (!"filtered".equals(effectiveMode) && !"all".equals(effectiveMode)) {
            throw new IllegalArgumentException("Le mode d'impression doit etre 'filtered' ou 'all'.");
        }

        List<Model> allItems = modelRepository.findAllByOrderByNameAsc().stream()
            .map(ModelMapper::toModel)
            .toList();

        String normalizedNameFilter = normalizeFilter(nameFilter);
        String normalizedDescriptionFilter = normalizeFilter(descriptionFilter);

        List<Model> filteredItems = "all".equals(effectiveMode)
            ? allItems
            : allItems.stream()
                .filter(item -> containsIgnoreCase(item.getName(), normalizedNameFilter))
                .filter(item -> containsIgnoreCase(item.getDescription(), normalizedDescriptionFilter))
                .toList();

        return new ModelPrintResponse(filteredItems, filteredItems.size(), OffsetDateTime.now().toString(), allItems.size());
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
