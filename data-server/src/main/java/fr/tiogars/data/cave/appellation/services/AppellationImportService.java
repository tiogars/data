package fr.tiogars.data.cave.appellation.services;

import static fr.tiogars.data.common.validation.TextValidationUtils.requireText;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.jspecify.annotations.NonNull;

import fr.tiogars.data.cave.appellation.entities.AppellationEntity;
import fr.tiogars.data.cave.appellation.forms.AppellationImportForm;
import fr.tiogars.data.cave.appellation.models.Appellation;
import fr.tiogars.data.cave.appellation.models.AppellationImportResult;
import fr.tiogars.data.cave.appellation.repositories.AppellationRepository;

@Service
public class AppellationImportService {
    private final AppellationRepository appellationRepository;
    public AppellationImportService(AppellationRepository appellationRepository) { this.appellationRepository = appellationRepository; }
    private record CandidateAppellation(String name) { }
    @Transactional
    public AppellationImportResult importAppellations(AppellationImportForm form) {
        List<CandidateAppellation> candidates = buildCandidates(form);
        if (candidates.isEmpty()) return new AppellationImportResult(List.of(), 0, 0, 0, 0, List.of());
        Set<String> existingNames = new HashSet<>(appellationRepository.findAllByOrderByNameAsc().stream().map(AppellationImportService::getName).toList());
        Set<String> duplicateNames = new LinkedHashSet<>();
        List<Appellation> imported = new java.util.ArrayList<>();
        int addedCount = 0;
        int alreadyExistsCount = 0;
        int invalidCount = 0;
        for (CandidateAppellation candidate : candidates) {
            try {
                String normalizedName = requireText(candidate.name(), "Le nom de l'appellation est obligatoire.");
                if (existingNames.contains(normalizedName)) { alreadyExistsCount++; duplicateNames.add(normalizedName); continue; }
                AppellationEntity entity = new AppellationEntity();
                AppellationCreationService.applyValues(entity, normalizedName);
                var saved = appellationRepository.save(entity);
                existingNames.add(normalizedName);
                imported.add(AppellationModelMapper.toModel(saved));
                addedCount++;
            } catch (RuntimeException _) { invalidCount++; }
        }
        return new AppellationImportResult(imported, addedCount, alreadyExistsCount + invalidCount, alreadyExistsCount, invalidCount, List.copyOf(duplicateNames));
    }

    private static String getName(@NonNull AppellationEntity entity) {
        return entity.getName();
    }
    private List<CandidateAppellation> buildCandidates(AppellationImportForm form) {
        if (form == null) {
            return List.of();
        }

        List<CandidateAppellation> candidates = new java.util.ArrayList<>();
        addItemCandidates(candidates, form.getItems());
        addTextCandidates(candidates, form.getText());
        return candidates;
    }

    private void addItemCandidates(List<CandidateAppellation> candidates, List<Appellation> items) {
        if (items == null) {
            return;
        }
        for (Appellation item : items) {
            if (item != null) {
                candidates.add(new CandidateAppellation(item.getName()));
            }
        }
    }

    private void addTextCandidates(List<CandidateAppellation> candidates, String text) {
        if (text == null || text.isBlank()) {
            return;
        }
        for (String line : text.split("\\R")) {
            String name = line.trim();
            if (!name.isEmpty()) {
                candidates.add(new CandidateAppellation(name));
            }
        }
    }
}
