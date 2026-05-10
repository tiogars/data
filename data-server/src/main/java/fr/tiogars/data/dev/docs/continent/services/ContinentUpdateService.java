package fr.tiogars.data.dev.docs.continent.services;

import org.springframework.stereotype.Service;

import fr.tiogars.data.common.exceptions.DataNotFoundException;
import fr.tiogars.data.dev.docs.continent.entities.ContinentEntity;
import fr.tiogars.data.dev.docs.continent.forms.ContinentUpdateForm;
import fr.tiogars.data.dev.docs.continent.models.Continent;
import fr.tiogars.data.dev.docs.continent.repositories.ContinentRepository;

/**
 * Service pour mettre à jour un continent.
 */
@Service
public class ContinentUpdateService {

    private final ContinentRepository continentRepository;
    private final ContinentCreationService continentCreationService;

    public ContinentUpdateService(ContinentRepository continentRepository, ContinentCreationService continentCreationService) {
        this.continentRepository = continentRepository;
        this.continentCreationService = continentCreationService;
    }

    public Continent updateContinent(String id, ContinentUpdateForm form) {
        ContinentEntity entity = continentRepository.findById(id)
            .orElseThrow(() -> new DataNotFoundException("Continent non trouve pour l'id: " + id));

        continentCreationService.validateUniqueCode(form.getCode(), id);
        continentCreationService.validateUniqueName(form.getName(), id);
        ContinentCreationService.applyValues(entity, form.getCode(), form.getName());

        return ContinentModelMapper.toModel(continentRepository.save(entity));
    }
}
