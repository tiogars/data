package fr.tiogars.data.cave.cepage.services;

import org.springframework.stereotype.Service;
import fr.tiogars.data.cave.cepage.repositories.CepageRepository;
import fr.tiogars.data.cave.cepage.models.Cepage;

@Service
public class CepageGetOneService {
    private final CepageRepository cepageRepository;
    public CepageGetOneService(CepageRepository cepageRepository) { this.cepageRepository = cepageRepository; }
    public Cepage getCepage(String id) { return cepageRepository.findById(id).map(CepageModelMapper::toModel).orElseThrow(() -> new fr.tiogars.data.common.exceptions.DataNotFoundException("Cépage non trouve pour l'id: " + id)); }
}
