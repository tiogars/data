package fr.tiogars.data.cave.cepage.services;

import static fr.tiogars.data.common.validation.TextValidationUtils.requireText;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.tiogars.data.cave.cepage.entities.CepageEntity;
import fr.tiogars.data.cave.cepage.forms.CepageImportForm;
import fr.tiogars.data.cave.cepage.models.Cepage;
import fr.tiogars.data.cave.cepage.models.CepageImportResult;
import fr.tiogars.data.cave.cepage.repositories.CepageRepository;

@Service
public class CepageImportService {
    private final CepageRepository cepageRepository;
    public CepageImportService(CepageRepository cepageRepository) { this.cepageRepository = cepageRepository; }
    private record CandidateCepage(String name) { }
    @Transactional
    public CepageImportResult importCepages(CepageImportForm form) {
        List<CandidateCepage> candidates = buildCandidates(form);
        if (candidates.isEmpty()) return new CepageImportResult(List.of(), 0, 0, 0, 0, List.of());
        Set<String> existingNames = new HashSet<>(cepageRepository.findAllByOrderByNameAsc().stream().map(CepageEntity::getName).toList());
        Set<String> duplicateNames = new LinkedHashSet<>();
        List<Cepage> imported = new java.util.ArrayList<>();
        int addedCount = 0; int alreadyExistsCount = 0; int invalidCount = 0;
        for (CandidateCepage candidate : candidates) {
            try {
                String normalizedName = requireText(candidate.name(), "Le nom du cépage est obligatoire.");
                if (existingNames.contains(normalizedName)) { alreadyExistsCount++; duplicateNames.add(normalizedName); continue; }
                CepageEntity entity = new CepageEntity();
                CepageCreationService.applyValues(entity, normalizedName);
                var saved = cepageRepository.save(entity);
                existingNames.add(normalizedName);
                imported.add(CepageModelMapper.toModel(saved));
                addedCount++;
            } catch (RuntimeException ex) { invalidCount++; }
        }
        return new CepageImportResult(imported, addedCount, alreadyExistsCount + invalidCount, alreadyExistsCount, invalidCount, List.copyOf(duplicateNames));
    }
    private List<CandidateCepage> buildCandidates(CepageImportForm form) {
        if (form == null) return List.of();
        List<CandidateCepage> candidates = new java.util.ArrayList<>();
        if (form.getItems() != null) for (Cepage item : form.getItems()) if (item != null) candidates.add(new CandidateCepage(item.getName()));
        if (form.getText() != null && !form.getText().isBlank()) for (String line : form.getText().split("\\R")) { String name = line == null ? null : line.trim(); if (name != null && !name.isEmpty()) candidates.add(new CandidateCepage(name)); }
        return candidates;
    }
}
