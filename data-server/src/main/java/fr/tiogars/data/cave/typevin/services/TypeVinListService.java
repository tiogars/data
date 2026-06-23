package fr.tiogars.data.cave.typevin.services;

import org.springframework.stereotype.Service;
import fr.tiogars.data.cave.typevin.repositories.TypeVinRepository;
import fr.tiogars.data.cave.typevin.models.TypeVinListResponse;

@Service
public class TypeVinListService {
    private final TypeVinRepository typeVinRepository;
    public TypeVinListService(TypeVinRepository typeVinRepository) { this.typeVinRepository = typeVinRepository; }
    public TypeVinListResponse listTypeVins() { var entities = typeVinRepository.findAllByOrderByNameAsc(); return new TypeVinListResponse(entities.stream().map(TypeVinModelMapper::toModel).toList(), entities.size()); }
}
