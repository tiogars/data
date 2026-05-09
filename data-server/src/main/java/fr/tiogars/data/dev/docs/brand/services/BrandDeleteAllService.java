package fr.tiogars.data.dev.docs.brand.services;

import org.springframework.stereotype.Service;

import fr.tiogars.data.dev.docs.brand.repositories.BrandRepository;

@Service
public class BrandDeleteAllService {

    private final BrandRepository brandRepository;

    public BrandDeleteAllService(BrandRepository brandRepository) {
        this.brandRepository = brandRepository;
    }

    public void deleteAllBrands() {
        brandRepository.deleteAllInBatch();
    }
}
