package fr.tiogars.data.dev.model.services;

import java.util.List;

import org.springframework.stereotype.Service;

import fr.tiogars.data.dev.model.models.Model;
import fr.tiogars.data.dev.model.models.ModelListResponse;
import fr.tiogars.data.dev.model.repositories.ModelRepository;

@Service
public class ModelExportService {

    private final ModelRepository modelRepository;

    public ModelExportService(ModelRepository modelRepository) {
        this.modelRepository = modelRepository;
    }

    public ModelListResponse exportModels() {
        List<Model> items = modelRepository.findAllByOrderByNameAsc().stream()
            .map(ModelMapper::toModel)
            .toList();

        return new ModelListResponse(items, items.size());
    }
}
