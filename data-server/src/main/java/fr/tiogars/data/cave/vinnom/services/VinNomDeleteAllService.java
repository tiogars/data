package fr.tiogars.data.cave.vinnom.services;

import org.springframework.stereotype.Service;

import fr.tiogars.data.cave.vinnom.repositories.VinNomRepository;

@Service
public class VinNomDeleteAllService {
    private final VinNomRepository vinNomRepository;
    public VinNomDeleteAllService(VinNomRepository vinNomRepository) { this.vinNomRepository = vinNomRepository; }
    public void deleteAllVinNoms() { vinNomRepository.deleteAllInBatch(); }
}
