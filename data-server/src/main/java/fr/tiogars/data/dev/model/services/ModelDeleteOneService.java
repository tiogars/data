package fr.tiogars.data.dev.model.services;

import org.springframework.stereotype.Service;

import fr.tiogars.data.common.exceptions.DataNotFoundException;
import fr.tiogars.data.dev.model.repositories.ModelRepository;

@Service
public class ModelDeleteOneService {

    private final ModelRepository modelRepository;

    public ModelDeleteOneService(ModelRepository modelRepository) {
        this.modelRepository = modelRepository;
    }

    public void deleteModel(String id) {
        if (!modelRepository.existsById(id)) {
            throw new DataNotFoundException("Modele non trouve pour l'id: " + id);
        }
        modelRepository.deleteById(id);
    }
}
