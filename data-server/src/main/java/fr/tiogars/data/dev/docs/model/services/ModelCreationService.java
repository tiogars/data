package fr.tiogars.data.dev.docs.model.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import fr.tiogars.data.dev.docs.model.entities.ModelAttributeEntity;
import fr.tiogars.data.dev.docs.model.entities.ModelEntity;
import fr.tiogars.data.dev.docs.model.forms.ModelCreationForm;
import fr.tiogars.data.dev.docs.model.models.ModelAttribute;
import fr.tiogars.data.dev.docs.model.models.Model;
import fr.tiogars.data.dev.docs.model.repositories.ModelRepository;

@Service
public class ModelCreationService {

    private final ModelRepository modelRepository;

    public ModelCreationService(ModelRepository modelRepository) {
        this.modelRepository = modelRepository;
    }

    public Model createModel(ModelCreationForm form) {
        validateUniqueName(form.getName(), null);

        ModelEntity entity = new ModelEntity();
        applyValues(entity, form.getName(), form.getDescription(), form.getModelAttributes());

        return ModelMapper.toModel(modelRepository.save(entity));
    }

    static void applyValues(ModelEntity entity, String name, String description, List<ModelAttribute> modelAttributes) {
        entity.setName(requireText(name, "Le nom du modele est obligatoire."));
        entity.setDescription(normalizeNullableText(description));

        entity.getModelAttributes().clear();
        for (ModelAttribute attribute : normalizeModelAttributes(modelAttributes)) {
            ModelAttributeEntity attributeEntity = new ModelAttributeEntity();
            attributeEntity.setName(requireText(attribute.getName(), "Le nom de l'attribut du modele est obligatoire."));
            attributeEntity.setDescription(normalizeNullableText(attribute.getDescription()));
            attributeEntity.setModel(entity);
            entity.getModelAttributes().add(attributeEntity);
        }
    }

    void validateUniqueName(String name, String currentId) {
        modelRepository.findByName(requireText(name, "Le nom du modele est obligatoire."))
            .filter(entity -> !entity.getId().equals(currentId))
            .ifPresent(entity -> {
                throw new IllegalArgumentException("Un modele avec ce nom existe deja.");
            });
    }

    static String requireText(String value, String message) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(message);
        }
        return value.trim();
    }

    static String normalizeNullableText(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return value.trim();
    }

    static List<ModelAttribute> normalizeModelAttributes(List<ModelAttribute> attributes) {
        if (attributes == null || attributes.isEmpty()) {
            return List.of();
        }

        List<ModelAttribute> normalized = new ArrayList<>();
        List<String> seenNames = new ArrayList<>();

        for (ModelAttribute attribute : attributes) {
            if (attribute == null) {
                continue;
            }

            String normalizedName = requireText(attribute.getName(), "Le nom de l'attribut du modele est obligatoire.");
            String uniqueKey = normalizedName.toLowerCase();
            if (seenNames.contains(uniqueKey)) {
                continue;
            }

            ModelAttribute normalizedAttribute = new ModelAttribute();
            normalizedAttribute.setName(normalizedName);
            normalizedAttribute.setDescription(normalizeNullableText(attribute.getDescription()));
            normalized.add(normalizedAttribute);
            seenNames.add(uniqueKey);
        }

        return normalized;
    }
}
