package fr.tiogars.data.products.brand.services;

import static fr.tiogars.data.common.validation.TextValidationUtils.requireText;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.tiogars.data.products.brand.entities.BrandEntity;
import fr.tiogars.data.products.brand.forms.BrandImportForm;
import fr.tiogars.data.products.brand.models.Brand;
import fr.tiogars.data.products.brand.models.BrandImportResult;
import fr.tiogars.data.products.brand.repositories.BrandRepository;

@Service
public class BrandImportService {

    private final BrandRepository brandRepository;

    public BrandImportService(BrandRepository brandRepository) {
        this.brandRepository = brandRepository;
    }

    private record CandidateBrand(String name, String description) { }

    @Transactional
    public BrandImportResult importBrands(BrandImportForm form) {
        List<CandidateBrand> candidates = buildCandidates(form);
        if (candidates.isEmpty()) {
            return new BrandImportResult(List.of(), 0, 0, 0, 0, List.of());
        }

        Set<String> existingNames = new HashSet<>(brandRepository.findAllByOrderByNameAsc().stream()
                .map(BrandImportService::getName)
            .toList());

        Set<String> duplicateNames = new LinkedHashSet<>();
        List<Brand> imported = new java.util.ArrayList<>();

        int addedCount = 0;
        int alreadyExistsCount = 0;
        int invalidCount = 0;

        for (CandidateBrand candidate : candidates) {
            try {
                String normalizedName = requireText(candidate.name(), "Le nom de la marque est obligatoire.");
                if (existingNames.contains(normalizedName)) {
                    alreadyExistsCount++;
                    duplicateNames.add(normalizedName);
                    continue;
                }

                BrandEntity entity = new BrandEntity();
                BrandCreationService.applyValues(entity, normalizedName, candidate.description());
                BrandEntity saved = brandRepository.save(entity);
                existingNames.add(normalizedName);
                imported.add(BrandModelMapper.toModel(saved));
                addedCount++;
            } catch (IllegalArgumentException ex) {
                invalidCount++;
            } catch (RuntimeException ex) {
                invalidCount++;
            }
        }

        return new BrandImportResult(
            imported,
            addedCount,
            alreadyExistsCount + invalidCount,
            alreadyExistsCount,
            invalidCount,
            List.copyOf(duplicateNames)
        );
    }

        private static String getName(@org.jspecify.annotations.NonNull BrandEntity entity) {
            return entity.getName();
        }
    private List<CandidateBrand> buildCandidates(BrandImportForm form) {
        if (form == null) {
            return List.of();
        }

        List<CandidateBrand> candidates = new java.util.ArrayList<>();

        if (form.getItems() != null) {
            for (Brand item : form.getItems()) {
                if (item == null) {
                    continue;
                }
                candidates.add(new CandidateBrand(item.getName(), item.getDescription()));
            }
        }

        String text = form.getText();
        if (text != null && !text.isBlank()) {
            String[] lines = text.split("\\R");
            for (String line : lines) {
                String name = line != null ? line.trim() : null;
                if (name == null || name.isEmpty()) {
                    continue;
                }
                candidates.add(new CandidateBrand(name, null));
            }
        }

        return candidates;
    }
}
