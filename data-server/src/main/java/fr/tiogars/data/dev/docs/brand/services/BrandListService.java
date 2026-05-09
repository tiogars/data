package fr.tiogars.data.dev.docs.brand.services;

import java.util.List;

import org.springframework.stereotype.Service;

import fr.tiogars.data.dev.docs.brand.entities.BrandEntity;
import fr.tiogars.data.dev.docs.brand.models.BrandListResponse;
import fr.tiogars.data.dev.docs.brand.repositories.BrandRepository;

@Service
public class BrandListService {

    private final BrandRepository brandRepository;

    public BrandListService(BrandRepository brandRepository) {
        this.brandRepository = brandRepository;
    }

    public BrandListResponse listBrands() {
        List<BrandEntity> entities = brandRepository.findAllByOrderByNameAsc();
        return new BrandListResponse(entities.stream().map(BrandModelMapper::toModel).toList(), entities.size());
    }
}
