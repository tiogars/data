package fr.tiogars.data.cave.contenant.services;

import static fr.tiogars.data.common.validation.TextValidationUtils.requireText;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.tiogars.data.cave.contenant.entities.ContenantEntity;
import fr.tiogars.data.cave.contenant.forms.ContenantImportForm;
import fr.tiogars.data.cave.contenant.models.Contenant;
import fr.tiogars.data.cave.contenant.models.ContenantImportResult;
import fr.tiogars.data.cave.contenant.repositories.ContenantRepository;

@Service
public class ContenantImportService {
    private final ContenantRepository contenantRepository;

    public ContenantImportService(ContenantRepository contenantRepository) {
        this.contenantRepository = contenantRepository;
    }

    private record CandidateContenant(String name, Integer volumeCl) {
    }

    @Transactional
    public ContenantImportResult importContenants(ContenantImportForm form) {
        List<CandidateContenant> candidates = buildCandidates(form);
        if (candidates.isEmpty())
            return new ContenantImportResult(List.of(), 0, 0, 0, 0, List.of());
        Set<String> existingNames = new HashSet<>(
                contenantRepository.findAllByOrderByNameAsc().stream().map(ContenantImportService::getName).toList());
        Set<String> duplicateNames = new LinkedHashSet<>();
        List<Contenant> imported = new java.util.ArrayList<>();
        int addedCount = 0;
        int alreadyExistsCount = 0;
        int invalidCount = 0;
        for (CandidateContenant candidate : candidates) {
            try {
                String normalizedName = requireText(candidate.name(), "Le nom du contenant est obligatoire.");
                if (existingNames.contains(normalizedName)) {
                    alreadyExistsCount++;
                    duplicateNames.add(normalizedName);
                    continue;
                }
                ContenantEntity entity = new ContenantEntity();
                ContenantCreationService.applyValues(entity, normalizedName, candidate.volumeCl());
                var saved = contenantRepository.save(entity);
                existingNames.add(normalizedName);
                imported.add(ContenantModelMapper.toModel(saved));
                addedCount++;
            } catch (RuntimeException ex) {
                invalidCount++;
            }
        }
        return new ContenantImportResult(imported, addedCount, alreadyExistsCount + invalidCount, alreadyExistsCount,
                invalidCount, List.copyOf(duplicateNames));
    }

    private static String getName(@NonNull ContenantEntity entity) {
        return entity.getName();
    }

    private List<CandidateContenant> buildCandidates(ContenantImportForm form) {
        if (form == null)
            return List.of();
        List<CandidateContenant> candidates = new java.util.ArrayList<>();
        if (form.getItems() != null)
            for (Contenant item : form.getItems())
                if (item != null)
                    candidates.add(new CandidateContenant(item.getName(), item.getVolumeCl()));
        if (form.getText() != null && !form.getText().isBlank())
            for (String line : form.getText().split("\\R")) {
                String name = line == null ? null : line.trim();
                if (name != null && !name.isEmpty())
                    candidates.add(new CandidateContenant(name, null));
            }
        return candidates;
    }
}
