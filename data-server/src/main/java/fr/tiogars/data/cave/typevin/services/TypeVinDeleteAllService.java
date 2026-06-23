package fr.tiogars.data.cave.typevin.services;

import org.springframework.stereotype.Service;
import fr.tiogars.data.cave.typevin.repositories.TypeVinRepository;

@Service
public class TypeVinDeleteAllService {
    private final TypeVinRepository typeVinRepository;
    public TypeVinDeleteAllService(TypeVinRepository typeVinRepository) { this.typeVinRepository = typeVinRepository; }
    public void deleteAllTypeVins() { typeVinRepository.deleteAllInBatch(); }
}
