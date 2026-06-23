package fr.tiogars.data.cave.vintag.services;

import static fr.tiogars.data.common.validation.TextValidationUtils.requireText;

import org.springframework.stereotype.Service;
import fr.tiogars.data.cave.vintag.entities.VinTagEntity;
import fr.tiogars.data.cave.vintag.forms.VinTagCreationForm;
import fr.tiogars.data.cave.vintag.models.VinTag;
import fr.tiogars.data.cave.vintag.repositories.VinTagRepository;

@Service
public class VinTagCreationService {
    private final VinTagRepository vinTagRepository;
    public VinTagCreationService(VinTagRepository vinTagRepository) { this.vinTagRepository = vinTagRepository; }
    public VinTag createVinTag(VinTagCreationForm form) {
        validateUniqueName(form.getName(), null);
        VinTagEntity entity = new VinTagEntity();
        applyValues(entity, form.getName());
        return VinTagModelMapper.toModel(vinTagRepository.save(entity));
    }
    static void applyValues(VinTagEntity entity, String name) { entity.setName(requireText(name, "Le nom du tag de vin est obligatoire.")); }
    void validateUniqueName(String name, String currentId) {
        vinTagRepository.findByName(requireText(name, "Le nom du tag de vin est obligatoire."))
            .filter(entity -> !entity.getId().equals(currentId))
            .ifPresent(entity -> { throw new IllegalArgumentException("Un tag de vin avec ce nom existe deja."); });
    }
}
