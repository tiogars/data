package fr.tiogars.data.dev.model.services;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.tiogars.data.dev.model.entities.ModelEntity;
import fr.tiogars.data.dev.model.models.Model;
import fr.tiogars.data.dev.model.models.ModelImportResult;
import fr.tiogars.data.dev.model.repositories.ModelRepository;

@Service
public class ModelImportService {

    private final ModelRepository modelRepository;

    public ModelImportService(ModelRepository modelRepository) {
        this.modelRepository = modelRepository;
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
            item.setModelAttributes(ModelCreationService.normalizeModelAttributes(item.getModelAttributes()));

            if (seenNames.add(normalizedName)) {
                uniqueItems.add(item);
            } else if (!duplicateNames.contains(normalizedName)) {
                duplicateNames.add(normalizedName);
            }
        }

        modelRepository.deleteAll();

        List<ModelEntity> entities = uniqueItems.stream()
            .map(item -> {
                ModelEntity entity = new ModelEntity();
                ModelCreationService.applyValues(entity, item.getName(), item.getDescription(), item.getModelAttributes());
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
