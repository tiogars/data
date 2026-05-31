package fr.tiogars.data.dev.model.services;

import java.util.List;

import org.springframework.stereotype.Service;

import fr.tiogars.data.dev.model.entities.ModelEntity;
import fr.tiogars.data.dev.model.models.ModelListResponse;
import fr.tiogars.data.dev.model.repositories.ModelRepository;

@Service
public class ModelListService {

    private final ModelRepository modelRepository;

    public ModelListService(ModelRepository modelRepository) {
        this.modelRepository = modelRepository;
    }

    public ModelListResponse listModels() {
        List<ModelEntity> entities = modelRepository.findAllByOrderByNameAsc();
        return new ModelListResponse(entities.stream().map(ModelMapper::toModel).toList(), entities.size());
    }
}
