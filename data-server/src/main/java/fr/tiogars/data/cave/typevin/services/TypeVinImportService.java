package fr.tiogars.data.cave.typevin.services;

import static fr.tiogars.data.common.validation.TextValidationUtils.requireText;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.tiogars.data.cave.typevin.entities.TypeVinEntity;
import fr.tiogars.data.cave.typevin.forms.TypeVinImportForm;
import fr.tiogars.data.cave.typevin.models.TypeVin;
import fr.tiogars.data.cave.typevin.models.TypeVinImportResult;
import fr.tiogars.data.cave.typevin.repositories.TypeVinRepository;

@Service
public class TypeVinImportService {
    private final TypeVinRepository typeVinRepository;
    public TypeVinImportService(TypeVinRepository typeVinRepository) { this.typeVinRepository = typeVinRepository; }
    private record CandidateTypeVin(String name) { }
    @Transactional
    public TypeVinImportResult importTypeVins(TypeVinImportForm form) {
        List<CandidateTypeVin> candidates = buildCandidates(form);
        if (candidates.isEmpty()) return new TypeVinImportResult(List.of(), 0, 0, 0, 0, List.of());
            Set<String> existingNames = new HashSet<>(typeVinRepository.findAllByOrderByNameAsc().stream().map(TypeVinImportService::getName).toList());
        Set<String> duplicateNames = new LinkedHashSet<>();
        List<TypeVin> imported = new java.util.ArrayList<>();
        int addedCount = 0; int alreadyExistsCount = 0; int invalidCount = 0;
        for (CandidateTypeVin candidate : candidates) {
            try {
                String normalizedName = requireText(candidate.name(), "Le nom du type de vin est obligatoire.");
                if (existingNames.contains(normalizedName)) { alreadyExistsCount++; duplicateNames.add(normalizedName); continue; }
                TypeVinEntity entity = new TypeVinEntity();
                TypeVinCreationService.applyValues(entity, normalizedName);
                var saved = typeVinRepository.save(entity);
                existingNames.add(normalizedName);
                imported.add(TypeVinModelMapper.toModel(saved));
                addedCount++;
            } catch (RuntimeException ex) { invalidCount++; }
        }
        return new TypeVinImportResult(imported, addedCount, alreadyExistsCount + invalidCount, alreadyExistsCount, invalidCount, List.copyOf(duplicateNames));
    }
        private static String getName(@org.jspecify.annotations.NonNull TypeVinEntity entity) { return entity.getName(); }
    private List<CandidateTypeVin> buildCandidates(TypeVinImportForm form) {
        if (form == null) return List.of();
        List<CandidateTypeVin> candidates = new java.util.ArrayList<>();
        if (form.getItems() != null) for (TypeVin item : form.getItems()) if (item != null) candidates.add(new CandidateTypeVin(item.getName()));
        if (form.getText() != null && !form.getText().isBlank()) for (String line : form.getText().split("\\R")) { String name = line == null ? null : line.trim(); if (name != null && !name.isEmpty()) candidates.add(new CandidateTypeVin(name)); }
        return candidates;
    }
}
