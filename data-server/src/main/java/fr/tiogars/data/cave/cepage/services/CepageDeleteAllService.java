package fr.tiogars.data.cave.cepage.services;

import org.springframework.stereotype.Service;
import fr.tiogars.data.cave.cepage.repositories.CepageRepository;

@Service
public class CepageDeleteAllService {
    private final CepageRepository cepageRepository;
    public CepageDeleteAllService(CepageRepository cepageRepository) { this.cepageRepository = cepageRepository; }
    public void deleteAllCepages() { cepageRepository.deleteAllInBatch(); }
}
