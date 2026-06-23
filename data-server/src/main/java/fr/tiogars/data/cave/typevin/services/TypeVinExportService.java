package fr.tiogars.data.cave.typevin.services;

import org.springframework.stereotype.Service;
import fr.tiogars.data.cave.typevin.repositories.TypeVinRepository;
import fr.tiogars.data.cave.typevin.models.TypeVinListResponse;

@Service
public class TypeVinExportService {
    private final TypeVinRepository typeVinRepository;
    public TypeVinExportService(TypeVinRepository typeVinRepository) { this.typeVinRepository = typeVinRepository; }
    public TypeVinListResponse exportTypeVins() { var items = typeVinRepository.findAllByOrderByNameAsc().stream().map(TypeVinModelMapper::toModel).toList(); return new TypeVinListResponse(items, items.size()); }
}
