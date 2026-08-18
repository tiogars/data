package fr.tiogars.data.cave.circonstance.services;

import static fr.tiogars.data.common.validation.TextValidationUtils.requireText;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.tiogars.data.cave.circonstance.entities.CirconstanceEntity;
import fr.tiogars.data.cave.circonstance.forms.CirconstanceImportForm;
import fr.tiogars.data.cave.circonstance.models.Circonstance;
import fr.tiogars.data.cave.circonstance.models.CirconstanceImportResult;
import fr.tiogars.data.cave.circonstance.repositories.CirconstanceRepository;

@Service
public class CirconstanceImportService {
    private final CirconstanceRepository circonstanceRepository;
    public CirconstanceImportService(CirconstanceRepository circonstanceRepository) { this.circonstanceRepository = circonstanceRepository; }
    private record CandidateCirconstance(String name) { }
    @Transactional
    public CirconstanceImportResult importCirconstances(CirconstanceImportForm form) {
        List<CandidateCirconstance> candidates = buildCandidates(form);
        if (candidates.isEmpty()) return new CirconstanceImportResult(List.of(), 0, 0, 0, 0, List.of());
        Set<String> existingNames = new HashSet<>(circonstanceRepository.findAllByOrderByNameAsc().stream().map(CirconstanceImportService::getName).toList());
        Set<String> duplicateNames = new LinkedHashSet<>();
        List<Circonstance> imported = new java.util.ArrayList<>();
        int addedCount = 0; int alreadyExistsCount = 0; int invalidCount = 0;
        for (CandidateCirconstance candidate : candidates) {
            try {
                String normalizedName = requireText(candidate.name(), "Le nom de la circonstance est obligatoire.");
                if (existingNames.contains(normalizedName)) { alreadyExistsCount++; duplicateNames.add(normalizedName); continue; }
                CirconstanceEntity entity = new CirconstanceEntity();
                CirconstanceCreationService.applyValues(entity, normalizedName);
                var saved = circonstanceRepository.save(entity);
                existingNames.add(normalizedName);
                imported.add(CirconstanceModelMapper.toModel(saved));
                addedCount++;
            } catch (RuntimeException ex) { invalidCount++; }
        }
        return new CirconstanceImportResult(imported, addedCount, alreadyExistsCount + invalidCount, alreadyExistsCount, invalidCount, List.copyOf(duplicateNames));
    }
    private static String getName(@NonNull CirconstanceEntity entity) { return entity.getName(); }
    private List<CandidateCirconstance> buildCandidates(CirconstanceImportForm form) {
        if (form == null) return List.of();
        List<CandidateCirconstance> candidates = new java.util.ArrayList<>();
        if (form.getItems() != null) for (Circonstance item : form.getItems()) if (item != null) candidates.add(new CandidateCirconstance(item.getName()));
        if (form.getText() != null && !form.getText().isBlank()) for (String line : form.getText().split("\\R")) { String name = line == null ? null : line.trim(); if (name != null && !name.isEmpty()) candidates.add(new CandidateCirconstance(name)); }
        return candidates;
    }
}
