package fr.tiogars.data.cave.cepage.services;

import org.springframework.stereotype.Service;
import fr.tiogars.data.cave.cepage.repositories.CepageRepository;

@Service
public class CepageDeleteOneService {
    private final CepageRepository cepageRepository;
    public CepageDeleteOneService(CepageRepository cepageRepository) { this.cepageRepository = cepageRepository; }
    public void deleteCepage(String id) { if (!cepageRepository.existsById(id)) throw new fr.tiogars.data.common.exceptions.DataNotFoundException("Cépage non trouve pour l'id: " + id); cepageRepository.deleteById(id); }
}
