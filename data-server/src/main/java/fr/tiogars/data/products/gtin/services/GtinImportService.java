package fr.tiogars.data.products.gtin.services;

import static fr.tiogars.data.common.validation.TextValidationUtils.normalizeNullableText;
import static fr.tiogars.data.common.validation.TextValidationUtils.requireText;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.tiogars.data.products.gtin.entities.GtinEntity;
import fr.tiogars.data.products.gtin.models.Gtin;
import fr.tiogars.data.products.gtin.models.GtinImportResult;
import fr.tiogars.data.products.gtin.repositories.GtinRepository;

@Service
public class GtinImportService {

    private final GtinRepository gtinRepository;

    public GtinImportService(GtinRepository gtinRepository) {
        this.gtinRepository = gtinRepository;
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
            String normalizedCode = requireText(item.getCode(), "Le code GTIN est obligatoire.");
            item.setCode(normalizedCode);
            item.setDescription(normalizeNullableText(item.getDescription()));

            if (seenCodes.add(normalizedCode)) {
                uniqueItems.add(item);
            } else if (!duplicateCodes.contains(normalizedCode)) {
                duplicateCodes.add(normalizedCode);
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
