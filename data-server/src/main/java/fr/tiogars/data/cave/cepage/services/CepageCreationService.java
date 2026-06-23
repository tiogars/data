package fr.tiogars.data.cave.cepage.services;

import static fr.tiogars.data.common.validation.TextValidationUtils.requireText;

import org.springframework.stereotype.Service;
import fr.tiogars.data.cave.cepage.entities.CepageEntity;
import fr.tiogars.data.cave.cepage.forms.CepageCreationForm;
import fr.tiogars.data.cave.cepage.models.Cepage;
import fr.tiogars.data.cave.cepage.repositories.CepageRepository;

@Service
public class CepageCreationService {
    private final CepageRepository cepageRepository;
    public CepageCreationService(CepageRepository cepageRepository) { this.cepageRepository = cepageRepository; }
    public Cepage createCepage(CepageCreationForm form) {
        validateUniqueName(form.getName(), null);
        CepageEntity entity = new CepageEntity();
        applyValues(entity, form.getName());
        return CepageModelMapper.toModel(cepageRepository.save(entity));
    }
    static void applyValues(CepageEntity entity, String name) { entity.setName(requireText(name, "Le nom du cépage est obligatoire.")); }
    void validateUniqueName(String name, String currentId) {
        cepageRepository.findByName(requireText(name, "Le nom du cépage est obligatoire."))
            .filter(entity -> !entity.getId().equals(currentId))
            .ifPresent(entity -> { throw new IllegalArgumentException("Un cépage avec ce nom existe deja."); });
    }
}
