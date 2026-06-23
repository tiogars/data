package fr.tiogars.data.cave.cepage.services;

import org.springframework.stereotype.Service;
import fr.tiogars.data.cave.cepage.repositories.CepageRepository;
import fr.tiogars.data.cave.cepage.models.CepageListResponse;

@Service
public class CepageListService {
    private final CepageRepository cepageRepository;
    public CepageListService(CepageRepository cepageRepository) { this.cepageRepository = cepageRepository; }
    public CepageListResponse listCepages() { var entities = cepageRepository.findAllByOrderByNameAsc(); return new CepageListResponse(entities.stream().map(CepageModelMapper::toModel).toList(), entities.size()); }
}
