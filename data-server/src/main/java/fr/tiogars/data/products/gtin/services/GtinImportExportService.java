package fr.tiogars.data.products.gtin.services;

import static fr.tiogars.data.common.validation.TextValidationUtils.normalizeNullableText;
import static fr.tiogars.data.common.validation.TextValidationUtils.requireText;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.tiogars.data.common.csv.CsvSupport;
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

    public String exportGtinsAsCsv() {
        List<Gtin> items = exportGtins().getItems();
        StringBuilder csv = new StringBuilder();
        csv.append("code,description\n");

        for (Gtin item : items) {
            csv.append(CsvSupport.escapeCsv(item != null ? item.getCode() : null));
            csv.append(',');
            csv.append(CsvSupport.escapeCsv(item != null ? item.getDescription() : null));
            csv.append('\n');
        }

        return csv.toString();
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

    @Transactional
    public GtinImportResult importGtinsFromCsv(String csvContent) {
        if (csvContent == null || csvContent.isBlank()) {
            return importGtins(List.of());
        }

        char delimiter = CsvSupport.detectDelimiter(csvContent);
        List<List<String>> rows = CsvSupport.parseCsvRows(csvContent, delimiter);
        if (rows.isEmpty()) {
            return importGtins(List.of());
        }

        CsvColumnMapping mapping = resolveColumnMapping(rows.getFirst());
        int startIndex = mapping.hasHeader() ? 1 : 0;
        List<Gtin> items = new ArrayList<>();

        for (int i = startIndex; i < rows.size(); i++) {
            List<String> row = rows.get(i);
            if (row.isEmpty()) {
                continue;
            }

            String code = CsvSupport.valueAt(row, mapping.codeIndex());
            String description = CsvSupport.valueAt(row, mapping.descriptionIndex());
            if ((code == null || code.isBlank()) && (description == null || description.isBlank())) {
                continue;
            }

            Gtin gtin = new Gtin();
            gtin.setCode(code);
            gtin.setDescription(description);
            items.add(gtin);
        }

        return importGtins(items);
    }

    private record CsvColumnMapping(boolean hasHeader, int codeIndex, int descriptionIndex) { }

    private static CsvColumnMapping resolveColumnMapping(List<String> firstRow) {
        if (firstRow == null || firstRow.isEmpty()) {
            return new CsvColumnMapping(false, 0, 1);
        }

        int codeIndex = -1;
        int descriptionIndex = -1;

        for (int i = 0; i < firstRow.size(); i++) {
            String normalizedHeader = CsvSupport.normalizeHeader(firstRow.get(i));
            if ("code".equals(normalizedHeader)) {
                codeIndex = i;
            }
            if ("description".equals(normalizedHeader)) {
                descriptionIndex = i;
            }
        }

        if (codeIndex >= 0 || descriptionIndex >= 0) {
            return new CsvColumnMapping(true, codeIndex >= 0 ? codeIndex : 0, descriptionIndex >= 0 ? descriptionIndex : 1);
        }

        return new CsvColumnMapping(false, 0, 1);
    }
}
