package fr.tiogars.data.cave.appellation.services;

import static fr.tiogars.data.common.validation.TextValidationUtils.requireText;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        Set<String> existingNames = new HashSet<>(appellationRepository.findAllByOrderByNameAsc().stream().map(AppellationEntity::getName).toList());
        Set<String> duplicateNames = new LinkedHashSet<>();
        List<Appellation> imported = new java.util.ArrayList<>();
        int addedCount = 0; int alreadyExistsCount = 0; int invalidCount = 0;
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
            } catch (RuntimeException ex) { invalidCount++; }
        }
        return new AppellationImportResult(imported, addedCount, alreadyExistsCount + invalidCount, alreadyExistsCount, invalidCount, List.copyOf(duplicateNames));
    }
    private List<CandidateAppellation> buildCandidates(AppellationImportForm form) {
        if (form == null) return List.of();
        List<CandidateAppellation> candidates = new java.util.ArrayList<>();
        if (form.getItems() != null) for (Appellation item : form.getItems()) if (item != null) candidates.add(new CandidateAppellation(item.getName()));
        if (form.getText() != null && !form.getText().isBlank()) for (String line : form.getText().split("\\R")) { String name = line == null ? null : line.trim(); if (name != null && !name.isEmpty()) candidates.add(new CandidateAppellation(name)); }
        return candidates;
    }
}
