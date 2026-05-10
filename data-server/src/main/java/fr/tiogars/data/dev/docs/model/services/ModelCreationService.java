package fr.tiogars.data.dev.docs.model.services;

import org.springframework.stereotype.Service;

import fr.tiogars.data.dev.docs.model.entities.ModelEntity;
import fr.tiogars.data.dev.docs.model.forms.ModelCreationForm;
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
        applyValues(entity, form.getName(), form.getDescription());

        return ModelMapper.toModel(modelRepository.save(entity));
    }

    static void applyValues(ModelEntity entity, String name, String description) {
        entity.setName(requireText(name, "Le nom du modele est obligatoire."));
        entity.setDescription(normalizeNullableText(description));
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
}
