package fr.tiogars.data.products.gtin.services;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.tiogars.data.products.gtin.entities.GtinEntity;
import fr.tiogars.data.products.gtin.models.Gtin;
import fr.tiogars.data.products.gtin.models.GtinImportResult;
import fr.tiogars.data.products.gtin.models.GtinListResponse;
import fr.tiogars.data.products.gtin.repositories.GtinRepository;

@Service
public class GtinImportExportService {

    private final GtinRepository gtinRepository;

    public GtinImportExportService(GtinRepository gtinRepository) {
        this.gtinRepository = gtinRepository;
    }

    public GtinListResponse exportGtins() {
        List<Gtin> items = gtinRepository.findAllByOrderByCodeAsc().stream()
            .map(GtinModelMapper::toModel)
            .toList();

        return new GtinListResponse(items, items.size());
    }

    @Transactional
    public GtinImportResult importGtins(List<Gtin> items) {
        List<Gtin> rawItems = items != null ? items : List.of();

        Set<String> seenCodes = new HashSet<>();
        List<Gtin> uniqueItems = new ArrayList<>();
        List<String> duplicateCodes = new ArrayList<>();

        for (Gtin item : rawItems) {
            if (item == null) {
                continue;
            }
            String normalizedCode = GtinCreationService.requireText(item.getCode(), "Le code GTIN est obligatoire.");
            item.setCode(normalizedCode);
            item.setDescription(GtinCreationService.normalizeNullableText(item.getDescription()));

            if (seenCodes.add(normalizedCode)) {
                uniqueItems.add(item);
            } else {
                if (!duplicateCodes.contains(normalizedCode)) {
                    duplicateCodes.add(normalizedCode);
                }
            }
        }

        gtinRepository.deleteAllInBatch();

        List<GtinEntity> entities = uniqueItems.stream()
            .map(item -> {
                GtinEntity entity = new GtinEntity();
                GtinCreationService.applyValues(entity, item.getCode(), item.getDescription());
                return entity;
            })
            .toList();

        gtinRepository.saveAll(entities);

        List<Gtin> imported = gtinRepository.findAllByOrderByCodeAsc().stream()
            .map(GtinModelMapper::toModel)
            .toList();

        return new GtinImportResult(imported, duplicateCodes);
    }
}
