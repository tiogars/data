package fr.tiogars.data.cave.appellation.services;

import static fr.tiogars.data.common.validation.TextValidationUtils.requireText;

import org.springframework.stereotype.Service;
import fr.tiogars.data.cave.appellation.entities.AppellationEntity;
import fr.tiogars.data.cave.appellation.forms.AppellationCreationForm;
import fr.tiogars.data.cave.appellation.models.Appellation;
import fr.tiogars.data.cave.appellation.repositories.AppellationRepository;

@Service
public class AppellationCreationService {
    private final AppellationRepository appellationRepository;
    public AppellationCreationService(AppellationRepository appellationRepository) { this.appellationRepository = appellationRepository; }
    public Appellation createAppellation(AppellationCreationForm form) {
        validateUniqueName(form.getName(), null);
        AppellationEntity entity = new AppellationEntity();
        applyValues(entity, form.getName());
        return AppellationModelMapper.toModel(appellationRepository.save(entity));
    }
    static void applyValues(AppellationEntity entity, String name) { entity.setName(requireText(name, "Le nom de l'appellation est obligatoire.")); }
    void validateUniqueName(String name, String currentId) {
        appellationRepository.findByName(requireText(name, "Le nom de l'appellation est obligatoire."))
            .filter(entity -> !entity.getId().equals(currentId))
            .ifPresent(entity -> { throw new IllegalArgumentException("Une appellation avec ce nom existe deja."); });
    }
}
