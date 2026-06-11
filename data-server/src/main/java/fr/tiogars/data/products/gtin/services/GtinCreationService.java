package fr.tiogars.data.products.gtin.services;

import static fr.tiogars.data.common.validation.TextValidationUtils.normalizeNullableText;
import static fr.tiogars.data.common.validation.TextValidationUtils.requireText;

import org.springframework.stereotype.Service;

import fr.tiogars.data.products.gtin.entities.GtinEntity;
import fr.tiogars.data.products.gtin.forms.GtinCreationForm;
import fr.tiogars.data.products.gtin.models.Gtin;
import fr.tiogars.data.products.gtin.repositories.GtinRepository;

@Service
public class GtinCreationService {

    private final GtinRepository gtinRepository;

    public GtinCreationService(GtinRepository gtinRepository) {
        this.gtinRepository = gtinRepository;
    }

    public Gtin createGtin(GtinCreationForm form) {
        validateUniqueCode(form.getCode(), null);

        GtinEntity entity = new GtinEntity();
        applyValues(entity, form.getCode(), form.getDescription());

        return GtinModelMapper.toModel(gtinRepository.save(entity));
    }

    static void applyValues(GtinEntity entity, String code, String description) {
        entity.setCode(requireText(code, "Le code GTIN est obligatoire."));
        entity.setDescription(normalizeNullableText(description));
    }

    void validateUniqueCode(String code, String currentId) {
        gtinRepository.findByCode(requireText(code, "Le code GTIN est obligatoire."))
            .filter(entity -> !entity.getId().equals(currentId))
            .ifPresent(entity -> {
                throw new IllegalArgumentException("Un GTIN avec ce code existe deja.");
            });
    }
}
