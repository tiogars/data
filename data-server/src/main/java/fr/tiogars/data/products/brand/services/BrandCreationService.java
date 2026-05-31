package fr.tiogars.data.products.brand.services;

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

    static String requireText(String value, String message) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(message);
        }
        return value.trim();
    }

    static String normalizeNullableText(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return value.trim();
    }
}
