package fr.tiogars.data.dev.docs.continent.services;

import java.util.List;

import org.springframework.stereotype.Service;

import fr.tiogars.data.dev.docs.continent.entities.ContinentEntity;
import fr.tiogars.data.dev.docs.continent.models.ContinentListResponse;
import fr.tiogars.data.dev.docs.continent.repositories.ContinentRepository;

/**
 * Service pour récupérer la liste des continents.
 */
@Service
public class ContinentListService {

    private final ContinentRepository continentRepository;

    public ContinentListService(ContinentRepository continentRepository) {
        this.continentRepository = continentRepository;
    }

    public ContinentListResponse listContinents() {
        List<ContinentEntity> entities = continentRepository.findAllByOrderByNameAsc();
        return new ContinentListResponse(entities.stream().map(ContinentModelMapper::toModel).toList(), entities.size());
    }
}
