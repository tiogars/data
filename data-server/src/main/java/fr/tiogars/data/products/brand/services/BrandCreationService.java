package fr.tiogars.data.products.brand.services;

import static fr.tiogars.data.common.validation.TextValidationUtils.normalizeNullableText;
import static fr.tiogars.data.common.validation.TextValidationUtils.requireText;

import org.springframework.stereotype.Service;

import fr.tiogars.data.products.brand.entities.BrandEntity;
import fr.tiogars.data.products.brand.forms.BrandCreationForm;
import fr.tiogars.data.products.brand.models.Brand;
import fr.tiogars.data.products.brand.repositories.BrandRepository;

@Service
public class BrandCreationService {

    private final BrandRepository brandRepository;

    public BrandCreationService(BrandRepository brandRepository) {
        this.brandRepository = brandRepository;
    }

    public Brand createBrand(BrandCreationForm form) {
        validateUniqueName(form.getName(), null);

        BrandEntity entity = new BrandEntity();
        applyValues(entity, form.getName(), form.getDescription());

        return BrandModelMapper.toModel(brandRepository.save(entity));
    }

    static void applyValues(BrandEntity entity, String name, String description) {
        entity.setName(requireText(name, "Le nom de la marque est obligatoire."));
        entity.setDescription(normalizeNullableText(description));
    }

    void validateUniqueName(String name, String currentId) {
        brandRepository.findByName(requireText(name, "Le nom de la marque est obligatoire."))
            .filter(entity -> !entity.getId().equals(currentId))
            .ifPresent(entity -> {
                throw new IllegalArgumentException("Une marque avec ce nom existe deja.");
            });
    }
}
