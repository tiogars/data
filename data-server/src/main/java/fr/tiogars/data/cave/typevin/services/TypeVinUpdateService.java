package fr.tiogars.data.cave.typevin.services;

import org.springframework.stereotype.Service;

import fr.tiogars.data.common.exceptions.DataNotFoundException;
import fr.tiogars.data.cave.typevin.entities.TypeVinEntity;
import fr.tiogars.data.cave.typevin.models.TypeVin;
import fr.tiogars.data.cave.typevin.repositories.TypeVinRepository;

@Service
public class TypeVinUpdateService {
    private final TypeVinRepository typeVinRepository;
    private final TypeVinCreationService typeVinCreationService;
    public TypeVinUpdateService(TypeVinRepository typeVinRepository, TypeVinCreationService typeVinCreationService) { this.typeVinRepository = typeVinRepository; this.typeVinCreationService = typeVinCreationService; }
    public TypeVin updateTypeVin(String id, TypeVin typeVin) {
        TypeVinEntity entity = typeVinRepository.findById(id).orElseThrow(() -> new DataNotFoundException("Type de vin non trouve pour l'id: " + id));
        typeVinCreationService.validateUniqueName(typeVin.getName(), id);
        TypeVinCreationService.applyValues(entity, typeVin.getName());
        return TypeVinModelMapper.toModel(typeVinRepository.save(entity));
    }
}
