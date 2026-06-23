package fr.tiogars.data.cave.typevin.services;

import org.springframework.stereotype.Service;
import fr.tiogars.data.cave.typevin.repositories.TypeVinRepository;

@Service
public class TypeVinDeleteOneService {
    private final TypeVinRepository typeVinRepository;
    public TypeVinDeleteOneService(TypeVinRepository typeVinRepository) { this.typeVinRepository = typeVinRepository; }
    public void deleteTypeVin(String id) { if (!typeVinRepository.existsById(id)) throw new fr.tiogars.data.common.exceptions.DataNotFoundException("Type de vin non trouve pour l'id: " + id); typeVinRepository.deleteById(id); }
}
