package fr.tiogars.data.cave.vinnom.services;

import org.springframework.stereotype.Service;

import fr.tiogars.data.cave.maison.repositories.MaisonRepository;
import fr.tiogars.data.cave.vinnom.models.VinNomListResponse;
import fr.tiogars.data.cave.vinnom.repositories.VinNomRepository;

@Service
public class VinNomListService {
    private final VinNomRepository vinNomRepository;
    private final MaisonRepository maisonRepository;
    public VinNomListService(VinNomRepository vinNomRepository, MaisonRepository maisonRepository) { this.vinNomRepository = vinNomRepository; this.maisonRepository = maisonRepository; }
    public VinNomListResponse listVinNoms() {
        var entities = vinNomRepository.findAllByOrderByNameAsc();
        return new VinNomListResponse(VinNomModelMapper.toModels(entities, maisonRepository), entities.size());
    }
}
