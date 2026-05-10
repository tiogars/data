package fr.tiogars.data.dev.docs.continent.services;

import org.springframework.stereotype.Service;

import fr.tiogars.data.dev.docs.continent.entities.ContinentEntity;
import fr.tiogars.data.dev.docs.continent.forms.ContinentCreationForm;
import fr.tiogars.data.dev.docs.continent.models.Continent;
import fr.tiogars.data.dev.docs.continent.repositories.ContinentRepository;

/**
 * Service pour créer un continent.
 */
@Service
public class ContinentCreationService {

    private final ContinentRepository continentRepository;

    public ContinentCreationService(ContinentRepository continentRepository) {
        this.continentRepository = continentRepository;
    }

    public Continent createContinent(ContinentCreationForm form) {
        validateUniqueCode(form.getCode(), null);
        validateUniqueName(form.getName(), null);

        ContinentEntity entity = new ContinentEntity();
        applyValues(entity, form.getCode(), form.getName());

        return ContinentModelMapper.toModel(continentRepository.save(entity));
    }

    static void applyValues(ContinentEntity entity, String code, String name) {
        entity.setCode(requireText(code, "Le code du continent est obligatoire."));
        entity.setName(requireText(name, "Le nom du continent est obligatoire."));
    }

    void validateUniqueCode(String code, String currentId) {
        continentRepository.findByCode(requireText(code, "Le code du continent est obligatoire."))
            .filter(entity -> !entity.getId().equals(currentId))
            .ifPresent(entity -> {
                throw new IllegalArgumentException("Un continent avec ce code existe deja.");
            });
    }

    void validateUniqueName(String name, String currentId) {
        continentRepository.findByName(requireText(name, "Le nom du continent est obligatoire."))
            .filter(entity -> !entity.getId().equals(currentId))
            .ifPresent(entity -> {
                throw new IllegalArgumentException("Un continent avec ce nom existe deja.");
            });
    }

    static String requireText(String value, String message) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(message);
        }
        return value.trim();
    }
}
