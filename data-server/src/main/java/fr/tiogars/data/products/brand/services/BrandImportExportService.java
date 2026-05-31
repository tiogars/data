package fr.tiogars.data.products.brand.services;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.tiogars.data.products.brand.entities.BrandEntity;
import fr.tiogars.data.products.brand.models.Brand;
import fr.tiogars.data.products.brand.models.BrandImportResult;
import fr.tiogars.data.products.brand.models.BrandListResponse;
import fr.tiogars.data.products.brand.repositories.BrandRepository;

@Service
public class BrandImportExportService {

    private final BrandRepository brandRepository;

    public BrandImportExportService(BrandRepository brandRepository) {
        this.brandRepository = brandRepository;
    }

    public BrandListResponse exportBrands() {
        List<Brand> items = brandRepository.findAllByOrderByNameAsc().stream()
            .map(BrandModelMapper::toModel)
            .toList();

        return new BrandListResponse(items, items.size());
    }

    @Transactional
    public BrandImportResult importBrands(List<Brand> items) {
        List<Brand> rawItems = items != null ? items : List.of();

        Set<String> seenNames = new HashSet<>();
        List<Brand> uniqueItems = new ArrayList<>();
        List<String> duplicateNames = new ArrayList<>();

        for (Brand item : rawItems) {
            if (item == null) {
                continue;
            }
            String normalizedName = BrandCreationService.requireText(item.getName(), "Le nom de la marque est obligatoire.");
            item.setName(normalizedName);
            item.setDescription(BrandCreationService.normalizeNullableText(item.getDescription()));

            if (seenNames.add(normalizedName)) {
                uniqueItems.add(item);
            } else {
                if (!duplicateNames.contains(normalizedName)) {
                    duplicateNames.add(normalizedName);
                }
            }
        }

        brandRepository.deleteAllInBatch();

        List<BrandEntity> entities = uniqueItems.stream()
            .map(item -> {
                BrandEntity entity = new BrandEntity();
                BrandCreationService.applyValues(entity, item.getName(), item.getDescription());
                return entity;
            })
            .toList();

        brandRepository.saveAll(entities);

        List<Brand> imported = brandRepository.findAllByOrderByNameAsc().stream()
            .map(BrandModelMapper::toModel)
            .toList();

        return new BrandImportResult(imported, duplicateNames);
    }
}
