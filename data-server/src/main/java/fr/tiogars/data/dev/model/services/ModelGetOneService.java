package fr.tiogars.data.dev.model.services;

import org.springframework.stereotype.Service;

import fr.tiogars.data.common.exceptions.DataNotFoundException;
import fr.tiogars.data.dev.model.models.Model;
import fr.tiogars.data.dev.model.repositories.ModelRepository;

@Service
public class ModelGetOneService {

    private final ModelRepository modelRepository;

    public ModelGetOneService(ModelRepository modelRepository) {
        this.modelRepository = modelRepository;
    }

    public Model getModel(String id) {
        return modelRepository.findById(id)
            .map(ModelMapper::toModel)
            .orElseThrow(() -> new DataNotFoundException("Modele non trouve pour l'id: " + id));
    }
}
