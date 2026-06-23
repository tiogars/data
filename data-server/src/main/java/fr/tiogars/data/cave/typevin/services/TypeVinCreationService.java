package fr.tiogars.data.cave.typevin.services;

import static fr.tiogars.data.common.validation.TextValidationUtils.requireText;

import org.springframework.stereotype.Service;
import fr.tiogars.data.cave.typevin.entities.TypeVinEntity;
import fr.tiogars.data.cave.typevin.forms.TypeVinCreationForm;
import fr.tiogars.data.cave.typevin.models.TypeVin;
import fr.tiogars.data.cave.typevin.repositories.TypeVinRepository;

@Service
public class TypeVinCreationService {
    private final TypeVinRepository typeVinRepository;
    public TypeVinCreationService(TypeVinRepository typeVinRepository) { this.typeVinRepository = typeVinRepository; }
    public TypeVin createTypeVin(TypeVinCreationForm form) {
        validateUniqueName(form.getName(), null);
        TypeVinEntity entity = new TypeVinEntity();
        applyValues(entity, form.getName());
        return TypeVinModelMapper.toModel(typeVinRepository.save(entity));
    }
    static void applyValues(TypeVinEntity entity, String name) { entity.setName(requireText(name, "Le nom du type de vin est obligatoire.")); }
    void validateUniqueName(String name, String currentId) {
        typeVinRepository.findByName(requireText(name, "Le nom du type de vin est obligatoire."))
            .filter(entity -> !entity.getId().equals(currentId))
            .ifPresent(entity -> { throw new IllegalArgumentException("Un type de vin avec ce nom existe deja."); });
    }
}
