package fr.tiogars.data.dev.model.services;

import org.springframework.stereotype.Service;

import fr.tiogars.data.dev.model.repositories.ModelRepository;

@Service
public class ModelDeleteAllService {

    private final ModelRepository modelRepository;

    public ModelDeleteAllService(ModelRepository modelRepository) {
        this.modelRepository = modelRepository;
    }

    public void deleteAllModels() {
        modelRepository.deleteAllInBatch();
    }
}
