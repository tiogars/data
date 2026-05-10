package fr.tiogars.data.dev.docs.model.services;

import org.springframework.stereotype.Service;

import fr.tiogars.data.common.exceptions.DataNotFoundException;
import fr.tiogars.data.dev.docs.model.entities.ModelEntity;
import fr.tiogars.data.dev.docs.model.models.Model;
import fr.tiogars.data.dev.docs.model.repositories.ModelRepository;

@Service
public class ModelUpdateService {

    private final ModelRepository modelRepository;
    private final ModelCreationService modelCreationService;

    public ModelUpdateService(ModelRepository modelRepository, ModelCreationService modelCreationService) {
        this.modelRepository = modelRepository;
        this.modelCreationService = modelCreationService;
    }

    public Model updateModel(String id, Model modelUpdate) {
        ModelEntity entity = modelRepository.findById(id)
            .orElseThrow(() -> new DataNotFoundException("Modele non trouve pour l'id: " + id));

        modelCreationService.validateUniqueName(modelUpdate.getName(), id);
        ModelCreationService.applyValues(entity, modelUpdate.getName(), modelUpdate.getDescription(), modelUpdate.getModelAttributes());

        return ModelMapper.toModel(modelRepository.save(entity));
    }
}
