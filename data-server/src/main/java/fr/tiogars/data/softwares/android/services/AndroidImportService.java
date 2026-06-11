package fr.tiogars.data.softwares.android.services;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.tiogars.data.softwares.android.entities.AndroidEntity;
import fr.tiogars.data.softwares.android.models.Android;
import fr.tiogars.data.softwares.android.models.AndroidImportResult;
import fr.tiogars.data.softwares.android.repositories.AndroidRepository;

@Service
public class AndroidImportService {

    private final AndroidRepository androidRepository;

    public AndroidImportService(AndroidRepository androidRepository) {
        this.androidRepository = androidRepository;
    }

    @Transactional
    public AndroidImportResult importAndroids(List<Android> items) {
        List<Android> rawItems = items != null ? items : List.of();

        Set<String> seenPackageNames = new HashSet<>();
        List<Android> uniqueItems = new ArrayList<>();
        List<String> duplicatePackageNames = new ArrayList<>();

        for (Android item : rawItems) {
            if (item == null) {
                continue;
            }

            String normalizedName = AndroidCreationService.requireText(item.getName(), "Le nom de l'application Android est obligatoire.");
            String normalizedPackageName = AndroidCreationService.requireText(item.getPackageName(), "Le package Android est obligatoire.");
            item.setName(normalizedName);
            item.setPackageName(normalizedPackageName);
            item.setCategory(AndroidCreationService.normalizeCategories(item.getCategory()));
            item.setDescription(AndroidCreationService.normalizeNullableText(item.getDescription()));
            item.setIcon(AndroidCreationService.normalizeNullableText(item.getIcon()));

            if (seenPackageNames.add(normalizedPackageName)) {
                uniqueItems.add(item);
            } else if (!duplicatePackageNames.contains(normalizedPackageName)) {
                duplicatePackageNames.add(normalizedPackageName);
            }
        }

        androidRepository.deleteAllInBatch();

        List<AndroidEntity> entities = uniqueItems.stream()
            .map(item -> {
                AndroidEntity entity = new AndroidEntity();
                AndroidCreationService.applyValues(entity, item.getName(), item.getPackageName(), item.getCategory(), item.getDescription(), item.getIcon());
                return entity;
            })
            .toList();

        androidRepository.saveAll(entities);

        List<Android> imported = androidRepository.findAllByOrderByNameAsc().stream()
            .map(AndroidModelMapper::toModel)
            .toList();

        return new AndroidImportResult(imported, duplicatePackageNames);
    }
}
