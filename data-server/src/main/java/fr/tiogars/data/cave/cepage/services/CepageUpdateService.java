package fr.tiogars.data.cave.cepage.services;

import org.springframework.stereotype.Service;

import fr.tiogars.data.common.exceptions.DataNotFoundException;
import fr.tiogars.data.cave.cepage.entities.CepageEntity;
import fr.tiogars.data.cave.cepage.models.Cepage;
import fr.tiogars.data.cave.cepage.repositories.CepageRepository;

@Service
public class CepageUpdateService {
    private final CepageRepository cepageRepository;
    private final CepageCreationService cepageCreationService;
    public CepageUpdateService(CepageRepository cepageRepository, CepageCreationService cepageCreationService) { this.cepageRepository = cepageRepository; this.cepageCreationService = cepageCreationService; }
    public Cepage updateCepage(String id, Cepage cepage) {
        CepageEntity entity = cepageRepository.findById(id).orElseThrow(() -> new DataNotFoundException("Cépage non trouve pour l'id: " + id));
        cepageCreationService.validateUniqueName(cepage.getName(), id);
        CepageCreationService.applyValues(entity, cepage.getName());
        return CepageModelMapper.toModel(cepageRepository.save(entity));
    }
}
