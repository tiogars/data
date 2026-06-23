package fr.tiogars.data.cave.vinnom.services;

import org.springframework.stereotype.Service;

import fr.tiogars.data.common.exceptions.DataNotFoundException;
import fr.tiogars.data.cave.maison.repositories.MaisonRepository;
import fr.tiogars.data.cave.vinnom.entities.VinNomEntity;
import fr.tiogars.data.cave.vinnom.models.VinNom;
import fr.tiogars.data.cave.vinnom.repositories.VinNomRepository;

@Service
public class VinNomGetOneService {
    private final VinNomRepository vinNomRepository;
    private final MaisonRepository maisonRepository;
    public VinNomGetOneService(VinNomRepository vinNomRepository, MaisonRepository maisonRepository) { this.vinNomRepository = vinNomRepository; this.maisonRepository = maisonRepository; }
    public VinNom getVinNom(String id) {
        VinNomEntity entity = vinNomRepository.findById(id).orElseThrow(() -> new DataNotFoundException("Vin non trouve pour l'id: " + id));
        return VinNomModelMapper.toModel(entity, maisonRepository);
    }
}
