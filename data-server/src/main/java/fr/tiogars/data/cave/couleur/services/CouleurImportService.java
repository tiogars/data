package fr.tiogars.data.cave.couleur.services;

import static fr.tiogars.data.common.validation.TextValidationUtils.requireText;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.tiogars.data.cave.couleur.entities.CouleurEntity;
import fr.tiogars.data.cave.couleur.forms.CouleurImportForm;
import fr.tiogars.data.cave.couleur.models.Couleur;
import fr.tiogars.data.cave.couleur.models.CouleurImportResult;
import fr.tiogars.data.cave.couleur.repositories.CouleurRepository;

@Service
public class CouleurImportService {
    private final CouleurRepository couleurRepository;
    public CouleurImportService(CouleurRepository couleurRepository) { this.couleurRepository = couleurRepository; }
    private record CandidateCouleur(String name) { }
    @Transactional
    public CouleurImportResult importCouleurs(CouleurImportForm form) {
        List<CandidateCouleur> candidates = buildCandidates(form);
        if (candidates.isEmpty()) return new CouleurImportResult(List.of(), 0, 0, 0, 0, List.of());
            Set<String> existingNames = new HashSet<>(couleurRepository.findAllByOrderByNameAsc().stream().map(CouleurImportService::getName).toList());
        Set<String> duplicateNames = new LinkedHashSet<>();
        List<Couleur> imported = new java.util.ArrayList<>();
        int addedCount = 0; int alreadyExistsCount = 0; int invalidCount = 0;
        for (CandidateCouleur candidate : candidates) {
            try {
                String normalizedName = requireText(candidate.name(), "Le nom de la couleur est obligatoire.");
                if (existingNames.contains(normalizedName)) { alreadyExistsCount++; duplicateNames.add(normalizedName); continue; }
                CouleurEntity entity = new CouleurEntity();
                CouleurCreationService.applyValues(entity, normalizedName);
                var saved = couleurRepository.save(entity);
                existingNames.add(normalizedName);
                imported.add(CouleurModelMapper.toModel(saved));
                addedCount++;
            } catch (RuntimeException ex) { invalidCount++; }
        }
        return new CouleurImportResult(imported, addedCount, alreadyExistsCount + invalidCount, alreadyExistsCount, invalidCount, List.copyOf(duplicateNames));
    }
        private static String getName(@org.jspecify.annotations.NonNull CouleurEntity entity) { return entity.getName(); }
    private List<CandidateCouleur> buildCandidates(CouleurImportForm form) {
        if (form == null) return List.of();
        List<CandidateCouleur> candidates = new java.util.ArrayList<>();
        if (form.getItems() != null) for (Couleur item : form.getItems()) if (item != null) candidates.add(new CandidateCouleur(item.getName()));
        if (form.getText() != null && !form.getText().isBlank()) for (String line : form.getText().split("\\R")) { String name = line == null ? null : line.trim(); if (name != null && !name.isEmpty()) candidates.add(new CandidateCouleur(name)); }
        return candidates;
    }
}
