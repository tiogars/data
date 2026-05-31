package fr.tiogars.data.locations.continent.services;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import fr.tiogars.data.locations.continent.entities.ContinentEntity;
import fr.tiogars.data.locations.continent.repositories.ContinentRepository;

/**
 * Initialise les continents par défaut au démarrage de l'application.
 */
@Component
public class ContinentDefaultDataInitializer implements CommandLineRunner {

    private final ContinentRepository continentRepository;

    public ContinentDefaultDataInitializer(ContinentRepository continentRepository) {
        this.continentRepository = continentRepository;
    }

    @Override
    public void run(String... args) {
        if (continentRepository.count() > 0) {
            return;
        }

        continentRepository.saveAll(List.of(
            createContinent("af", "Afrique"),
            createContinent("an", "Antarctique"),
            createContinent("as", "Asie"),
            createContinent("eu", "Europe"),
            createContinent("na", "Amérique du Nord"),
            createContinent("oc", "Océanie"),
            createContinent("sa", "Amérique du Sud")
        ));
    }

    private ContinentEntity createContinent(String code, String name) {
        ContinentEntity entity = new ContinentEntity();
        entity.setCode(code);
        entity.setName(name);
        return entity;
    }
}
