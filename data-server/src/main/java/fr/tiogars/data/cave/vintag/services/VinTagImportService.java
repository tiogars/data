package fr.tiogars.data.cave.vintag.services;

import static fr.tiogars.data.common.validation.TextValidationUtils.requireText;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.tiogars.data.cave.vintag.entities.VinTagEntity;
import fr.tiogars.data.cave.vintag.forms.VinTagImportForm;
import fr.tiogars.data.cave.vintag.models.VinTag;
import fr.tiogars.data.cave.vintag.models.VinTagImportResult;
import fr.tiogars.data.cave.vintag.repositories.VinTagRepository;

@Service
public class VinTagImportService {
    private final VinTagRepository vinTagRepository;
    public VinTagImportService(VinTagRepository vinTagRepository) { this.vinTagRepository = vinTagRepository; }
    private record CandidateVinTag(String name) { }
    @Transactional
    public VinTagImportResult importVinTags(VinTagImportForm form) {
        List<CandidateVinTag> candidates = buildCandidates(form);
        if (candidates.isEmpty()) return new VinTagImportResult(List.of(), 0, 0, 0, 0, List.of());
        Set<String> existingNames = new HashSet<>(vinTagRepository.findAllByOrderByNameAsc().stream().map(VinTagEntity::getName).toList());
        Set<String> duplicateNames = new LinkedHashSet<>();
        List<VinTag> imported = new java.util.ArrayList<>();
        int addedCount = 0; int alreadyExistsCount = 0; int invalidCount = 0;
        for (CandidateVinTag candidate : candidates) {
            try {
                String normalizedName = requireText(candidate.name(), "Le nom du tag de vin est obligatoire.");
                if (existingNames.contains(normalizedName)) { alreadyExistsCount++; duplicateNames.add(normalizedName); continue; }
                VinTagEntity entity = new VinTagEntity();
                VinTagCreationService.applyValues(entity, normalizedName);
                var saved = vinTagRepository.save(entity);
                existingNames.add(normalizedName);
                imported.add(VinTagModelMapper.toModel(saved));
                addedCount++;
            } catch (RuntimeException ex) { invalidCount++; }
        }
        return new VinTagImportResult(imported, addedCount, alreadyExistsCount + invalidCount, alreadyExistsCount, invalidCount, List.copyOf(duplicateNames));
    }
    private List<CandidateVinTag> buildCandidates(VinTagImportForm form) {
        if (form == null) return List.of();
        List<CandidateVinTag> candidates = new java.util.ArrayList<>();
        if (form.getItems() != null) for (VinTag item : form.getItems()) if (item != null) candidates.add(new CandidateVinTag(item.getName()));
        if (form.getText() != null && !form.getText().isBlank()) for (String line : form.getText().split("\\R")) { String name = line == null ? null : line.trim(); if (name != null && !name.isEmpty()) candidates.add(new CandidateVinTag(name)); }
        return candidates;
    }
}
