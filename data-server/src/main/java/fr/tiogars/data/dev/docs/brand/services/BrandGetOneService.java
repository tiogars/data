package fr.tiogars.data.dev.docs.brand.services;

import org.springframework.stereotype.Service;

import fr.tiogars.data.common.exceptions.DataNotFoundException;
import fr.tiogars.data.dev.docs.brand.models.Brand;
import fr.tiogars.data.dev.docs.brand.repositories.BrandRepository;

@Service
public class BrandGetOneService {

    private final BrandRepository brandRepository;

    public BrandGetOneService(BrandRepository brandRepository) {
        this.brandRepository = brandRepository;
    }

    public Brand getBrand(String id) {
        return brandRepository.findById(id)
            .map(BrandModelMapper::toModel)
            .orElseThrow(() -> new DataNotFoundException("Marque non trouve pour l'id: " + id));
    }
}
