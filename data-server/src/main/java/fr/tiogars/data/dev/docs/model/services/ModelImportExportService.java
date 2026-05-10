package fr.tiogars.data.dev.docs.model.services;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.tiogars.data.dev.docs.model.entities.ModelEntity;
import fr.tiogars.data.dev.docs.model.models.Model;
import fr.tiogars.data.dev.docs.model.models.ModelImportResult;
import fr.tiogars.data.dev.docs.model.models.ModelListResponse;
import fr.tiogars.data.dev.docs.model.repositories.ModelRepository;

@Service
public class ModelImportExportService {

    private final ModelRepository modelRepository;

    public ModelImportExportService(ModelRepository modelRepository) {
        this.modelRepository = modelRepository;
    }

    public ModelListResponse exportModels() {
        List<Model> items = modelRepository.findAllByOrderByNameAsc().stream()
            .map(ModelMapper::toModel)
            .toList();

        return new ModelListResponse(items, items.size());
    }

    @Transactional
    public ModelImportResult importModels(List<Model> items) {
        List<Model> rawItems = items != null ? items : List.of();

        Set<String> seenNames = new HashSet<>();
        List<Model> uniqueItems = new ArrayList<>();
        List<String> duplicateNames = new ArrayList<>();

        for (Model item : rawItems) {
            if (item == null) {
                continue;
            }
            String normalizedName = ModelCreationService.requireText(item.getName(), "Le nom du modele est obligatoire.");
            item.setName(normalizedName);
            item.setDescription(ModelCreationService.normalizeNullableText(item.getDescription()));

            if (seenNames.add(normalizedName)) {
                uniqueItems.add(item);
            } else {
                if (!duplicateNames.contains(normalizedName)) {
                    duplicateNames.add(normalizedName);
                }
            }
        }

        modelRepository.deleteAllInBatch();

        List<ModelEntity> entities = uniqueItems.stream()
            .map(item -> {
                ModelEntity entity = new ModelEntity();
                ModelCreationService.applyValues(entity, item.getName(), item.getDescription());
                return entity;
            })
            .toList();

        modelRepository.saveAll(entities);

        List<Model> imported = modelRepository.findAllByOrderByNameAsc().stream()
            .map(ModelMapper::toModel)
            .toList();

        return new ModelImportResult(imported, duplicateNames);
    }
}
