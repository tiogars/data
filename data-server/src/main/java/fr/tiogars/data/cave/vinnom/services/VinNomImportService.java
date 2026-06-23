package fr.tiogars.data.cave.vinnom.services;

import static fr.tiogars.data.common.validation.TextValidationUtils.normalizeNullableText;
import static fr.tiogars.data.common.validation.TextValidationUtils.requireText;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.tiogars.data.cave.maison.repositories.MaisonRepository;
import fr.tiogars.data.cave.vinnom.entities.VinNomEntity;
import fr.tiogars.data.cave.vinnom.forms.VinNomImportForm;
import fr.tiogars.data.cave.vinnom.models.VinNom;
import fr.tiogars.data.cave.vinnom.models.VinNomImportResult;
import fr.tiogars.data.cave.vinnom.repositories.VinNomRepository;

@Service
public class VinNomImportService {
    private final VinNomRepository vinNomRepository;
    private final MaisonRepository maisonRepository;
    public VinNomImportService(VinNomRepository vinNomRepository, MaisonRepository maisonRepository) { this.vinNomRepository = vinNomRepository; this.maisonRepository = maisonRepository; }
    private record CandidateVinNom(String name, String maisonId) { }
    @Transactional
    public VinNomImportResult importVinNoms(VinNomImportForm form) {
        List<CandidateVinNom> candidates = buildCandidates(form);
        if (candidates.isEmpty()) return new VinNomImportResult(List.of(), 0, 0, 0, 0, List.of());
        Set<String> existingKeys = new HashSet<>(vinNomRepository.findAllByOrderByNameAsc().stream().map(entity -> toKey(entity.getName(), entity.getMaisonId())).toList());
        Set<String> duplicateNames = new LinkedHashSet<>();
        List<VinNom> imported = new java.util.ArrayList<>();
        int addedCount = 0;
        int alreadyExistsCount = 0;
        int invalidCount = 0;
        for (CandidateVinNom candidate : candidates) {
            try {
                String normalizedName = requireText(candidate.name(), "Le nom du vin est obligatoire.");
                String normalizedMaisonId = normalizeNullableText(candidate.maisonId());
                String key = toKey(normalizedName, normalizedMaisonId);
                if (existingKeys.contains(key)) {
                    alreadyExistsCount++;
                    duplicateNames.add(formatDuplicateValue(normalizedName, normalizedMaisonId));
                    continue;
                }
                VinNomEntity entity = new VinNomEntity();
                VinNomCreationService.applyValues(entity, normalizedName, normalizedMaisonId, maisonRepository);
                VinNomEntity saved = vinNomRepository.save(entity);
                existingKeys.add(key);
                imported.add(VinNomModelMapper.toModel(saved, maisonRepository));
                addedCount++;
            } catch (RuntimeException ex) {
                invalidCount++;
            }
        }
        return new VinNomImportResult(imported, addedCount, alreadyExistsCount + invalidCount, alreadyExistsCount, invalidCount, List.copyOf(duplicateNames));
    }
    private List<CandidateVinNom> buildCandidates(VinNomImportForm form) {
        if (form == null) return List.of();
        List<CandidateVinNom> candidates = new java.util.ArrayList<>();
        if (form.getItems() != null) for (VinNom item : form.getItems()) if (item != null) candidates.add(new CandidateVinNom(item.getName(), item.getMaisonId()));
        if (form.getText() != null && !form.getText().isBlank()) for (String line : form.getText().split("\\R")) { String name = line == null ? null : line.trim(); if (name != null && !name.isEmpty()) candidates.add(new CandidateVinNom(name, null)); }
        return candidates;
    }
    private static String toKey(String name, String maisonId) { return (name == null ? "" : name) + "|" + (maisonId == null ? "" : maisonId); }
    private static String formatDuplicateValue(String name, String maisonId) { return maisonId == null ? name : name + " (" + maisonId + ")"; }
}
