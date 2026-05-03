package fr.tiogars.data.dev.docs.gtin.services;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.tiogars.data.dev.docs.gtin.entities.GtinEntity;
import fr.tiogars.data.dev.docs.gtin.models.Gtin;
import fr.tiogars.data.dev.docs.gtin.models.GtinListResponse;
import fr.tiogars.data.dev.docs.gtin.repositories.GtinRepository;

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
    public GtinListResponse importGtins(List<Gtin> items) {
        List<Gtin> importedItems = items != null ? items : List.of();
        validateImport(importedItems);

        gtinRepository.deleteAllInBatch();

        List<GtinEntity> entities = importedItems.stream()
            .map(item -> {
                GtinEntity entity = new GtinEntity();
                GtinCreationService.applyValues(entity, item.getCode(), item.getDescription());
                return entity;
            })
            .toList();

        gtinRepository.saveAll(entities);
        return exportGtins();
    }

    private void validateImport(List<Gtin> items) {
        Set<String> seenCodes = new HashSet<>();

        for (Gtin item : items) {
            if (item == null) {
                throw new IllegalArgumentException("Chaque element GTIN importe doit etre valide.");
            }

            String normalizedCode = GtinCreationService.requireText(item.getCode(), "Le code GTIN est obligatoire.");
            item.setCode(normalizedCode);
            item.setDescription(GtinCreationService.normalizeNullableText(item.getDescription()));

            if (!seenCodes.add(normalizedCode)) {
                throw new IllegalArgumentException("Le fichier d'import contient des codes GTIN en doublon.");
            }
        }
    }
}
