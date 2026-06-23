package fr.tiogars.data.cave.typevin.services;

import org.springframework.stereotype.Service;
import fr.tiogars.data.cave.typevin.repositories.TypeVinRepository;
import fr.tiogars.data.cave.typevin.models.TypeVin;

@Service
public class TypeVinGetOneService {
    private final TypeVinRepository typeVinRepository;
    public TypeVinGetOneService(TypeVinRepository typeVinRepository) { this.typeVinRepository = typeVinRepository; }
    public TypeVin getTypeVin(String id) { return typeVinRepository.findById(id).map(TypeVinModelMapper::toModel).orElseThrow(() -> new fr.tiogars.data.common.exceptions.DataNotFoundException("Type de vin non trouve pour l'id: " + id)); }
}
