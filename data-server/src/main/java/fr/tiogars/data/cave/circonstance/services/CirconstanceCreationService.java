package fr.tiogars.data.cave.circonstance.services;

import static fr.tiogars.data.common.validation.TextValidationUtils.requireText;

import org.springframework.stereotype.Service;
import fr.tiogars.data.cave.circonstance.entities.CirconstanceEntity;
import fr.tiogars.data.cave.circonstance.forms.CirconstanceCreationForm;
import fr.tiogars.data.cave.circonstance.models.Circonstance;
import fr.tiogars.data.cave.circonstance.repositories.CirconstanceRepository;

@Service
public class CirconstanceCreationService {
    private final CirconstanceRepository circonstanceRepository;
    public CirconstanceCreationService(CirconstanceRepository circonstanceRepository) { this.circonstanceRepository = circonstanceRepository; }
    public Circonstance createCirconstance(CirconstanceCreationForm form) {
        validateUniqueName(form.getName(), null);
        CirconstanceEntity entity = new CirconstanceEntity();
        applyValues(entity, form.getName());
        return CirconstanceModelMapper.toModel(circonstanceRepository.save(entity));
    }
    static void applyValues(CirconstanceEntity entity, String name) { entity.setName(requireText(name, "Le nom de la circonstance est obligatoire.")); }
    void validateUniqueName(String name, String currentId) {
        circonstanceRepository.findByName(requireText(name, "Le nom de la circonstance est obligatoire."))
            .filter(entity -> !entity.getId().equals(currentId))
            .ifPresent(entity -> { throw new IllegalArgumentException("Une circonstance avec ce nom existe deja."); });
    }
}
