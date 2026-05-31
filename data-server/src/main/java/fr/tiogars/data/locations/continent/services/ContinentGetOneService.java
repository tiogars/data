package fr.tiogars.data.locations.continent.services;

import org.springframework.stereotype.Service;

import fr.tiogars.data.common.exceptions.DataNotFoundException;
import fr.tiogars.data.locations.continent.models.Continent;
import fr.tiogars.data.locations.continent.repositories.ContinentRepository;

/**
 * Service pour récupérer un continent par son ID.
 */
@Service
public class ContinentGetOneService {

    private final ContinentRepository continentRepository;

    public ContinentGetOneService(ContinentRepository continentRepository) {
        this.continentRepository = continentRepository;
    }

    public Continent getContinent(String id) {
        return continentRepository.findById(id)
            .map(ContinentModelMapper::toModel)
            .orElseThrow(() -> new DataNotFoundException("Continent non trouve pour l'id: " + id));
    }
}
