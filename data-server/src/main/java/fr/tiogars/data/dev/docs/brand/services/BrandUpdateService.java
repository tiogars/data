package fr.tiogars.data.dev.docs.brand.services;

import org.springframework.stereotype.Service;

import fr.tiogars.data.common.exceptions.DataNotFoundException;
import fr.tiogars.data.dev.docs.brand.entities.BrandEntity;
import fr.tiogars.data.dev.docs.brand.models.Brand;
import fr.tiogars.data.dev.docs.brand.repositories.BrandRepository;

@Service
public class BrandUpdateService {

    private final BrandRepository brandRepository;
    private final BrandCreationService brandCreationService;

    public BrandUpdateService(BrandRepository brandRepository, BrandCreationService brandCreationService) {
        this.brandRepository = brandRepository;
        this.brandCreationService = brandCreationService;
    }

    public Brand updateBrand(String id, Brand brandUpdate) {
        BrandEntity entity = brandRepository.findById(id)
            .orElseThrow(() -> new DataNotFoundException("Marque non trouve pour l'id: " + id));

        brandCreationService.validateUniqueName(brandUpdate.getName(), id);
        BrandCreationService.applyValues(entity, brandUpdate.getName(), brandUpdate.getDescription());

        return BrandModelMapper.toModel(brandRepository.save(entity));
    }
}
