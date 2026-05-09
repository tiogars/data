package fr.tiogars.data.dev.docs.brand.services;

import org.springframework.stereotype.Service;

import fr.tiogars.data.common.exceptions.DataNotFoundException;
import fr.tiogars.data.dev.docs.brand.repositories.BrandRepository;

@Service
public class BrandDeleteOneService {

    private final BrandRepository brandRepository;

    public BrandDeleteOneService(BrandRepository brandRepository) {
        this.brandRepository = brandRepository;
    }

    public void deleteBrand(String id) {
        if (!brandRepository.existsById(id)) {
            throw new DataNotFoundException("Marque non trouve pour l'id: " + id);
        }
        brandRepository.deleteById(id);
    }
}
