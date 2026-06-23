package fr.tiogars.data.cave.vinnom.services;

import org.springframework.stereotype.Service;

import fr.tiogars.data.common.exceptions.DataNotFoundException;
import fr.tiogars.data.cave.maison.repositories.MaisonRepository;
import fr.tiogars.data.cave.vinnom.entities.VinNomEntity;
import fr.tiogars.data.cave.vinnom.models.VinNom;
import fr.tiogars.data.cave.vinnom.repositories.VinNomRepository;

@Service
public class VinNomUpdateService {
    private final VinNomRepository vinNomRepository;
    private final VinNomCreationService vinNomCreationService;
    private final MaisonRepository maisonRepository;
    public VinNomUpdateService(VinNomRepository vinNomRepository, VinNomCreationService vinNomCreationService, MaisonRepository maisonRepository) { this.vinNomRepository = vinNomRepository; this.vinNomCreationService = vinNomCreationService; this.maisonRepository = maisonRepository; }
    public VinNom updateVinNom(String id, VinNom vinNom) {
        VinNomEntity entity = vinNomRepository.findById(id).orElseThrow(() -> new DataNotFoundException("Vin non trouve pour l'id: " + id));
        vinNomCreationService.validateUniqueCombination(vinNom.getName(), vinNom.getMaisonId(), id);
        VinNomCreationService.applyValues(entity, vinNom.getName(), vinNom.getMaisonId(), maisonRepository);
        return VinNomModelMapper.toModel(vinNomRepository.save(entity), maisonRepository);
    }
}
