package fr.tiogars.data.cave.vinnom.services;

import org.springframework.stereotype.Service;

import fr.tiogars.data.common.exceptions.DataNotFoundException;
import fr.tiogars.data.cave.vinnom.repositories.VinNomRepository;

@Service
public class VinNomDeleteOneService {
    private final VinNomRepository vinNomRepository;
    public VinNomDeleteOneService(VinNomRepository vinNomRepository) { this.vinNomRepository = vinNomRepository; }
    public void deleteVinNom(String id) {
        if (!vinNomRepository.existsById(id)) throw new DataNotFoundException("Vin non trouve pour l'id: " + id);
        vinNomRepository.deleteById(id);
    }
}
