package fr.tiogars.data.products.brand.services;

import java.util.List;

import org.springframework.stereotype.Service;

import fr.tiogars.data.products.brand.models.Brand;
import fr.tiogars.data.products.brand.models.BrandListResponse;
import fr.tiogars.data.products.brand.repositories.BrandRepository;

@Service
public class BrandExportService {

    private final BrandRepository brandRepository;

    public BrandExportService(BrandRepository brandRepository) {
        this.brandRepository = brandRepository;
    }

    public BrandListResponse exportBrands() {
        List<Brand> items = brandRepository.findAllByOrderByNameAsc().stream()
            .map(BrandModelMapper::toModel)
            .toList();

        return new BrandListResponse(items, items.size());
    }
}
