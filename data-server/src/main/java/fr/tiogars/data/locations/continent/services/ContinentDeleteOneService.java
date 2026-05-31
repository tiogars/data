package fr.tiogars.data.locations.continent.services;

import org.springframework.stereotype.Service;

import fr.tiogars.data.common.exceptions.DataNotFoundException;
import fr.tiogars.data.locations.continent.repositories.ContinentRepository;

/**
 * Service pour supprimer un continent.
 */
@Service
public class ContinentDeleteOneService {

    private final ContinentRepository continentRepository;

    public ContinentDeleteOneService(ContinentRepository continentRepository) {
        this.continentRepository = continentRepository;
    }

    public void deleteContinent(String id) {
        if (!continentRepository.existsById(id)) {
            throw new DataNotFoundException("Continent non trouve pour l'id: " + id);
        }
        continentRepository.deleteById(id);
    }
}
