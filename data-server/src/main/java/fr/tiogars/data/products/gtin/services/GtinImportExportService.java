package fr.tiogars.data.products.gtin.services;

import static fr.tiogars.data.common.validation.TextValidationUtils.normalizeNullableText;
import static fr.tiogars.data.common.validation.TextValidationUtils.requireText;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
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

    public String exportGtinsAsCsv() {
        List<Gtin> items = exportGtins().getItems();
        StringBuilder csv = new StringBuilder();
        csv.append("code,description\n");

        for (Gtin item : items) {
            csv.append(escapeCsv(item != null ? item.getCode() : null));
            csv.append(',');
            csv.append(escapeCsv(item != null ? item.getDescription() : null));
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

        char delimiter = detectDelimiter(csvContent);
        List<List<String>> rows = parseCsvRows(csvContent, delimiter);
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

            String code = valueAt(row, mapping.codeIndex());
            String description = valueAt(row, mapping.descriptionIndex());
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
            String normalizedHeader = normalizeHeader(firstRow.get(i));
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

    private static String normalizeHeader(String value) {
        if (value == null) {
            return "";
        }
        return value
            .replace("\uFEFF", "")
            .trim()
            .toLowerCase(Locale.ROOT);
    }

    private static String valueAt(List<String> row, int index) {
        if (row == null || index < 0 || index >= row.size()) {
            return null;
        }
        return row.get(index);
    }

    private static char detectDelimiter(String content) {
        if (content == null || content.isBlank()) {
            return ',';
        }

        String[] lines = content.split("\\R");
        for (String line : lines) {
            if (line == null || line.isBlank()) {
                continue;
            }
            int semicolonCount = 0;
            int commaCount = 0;
            for (int i = 0; i < line.length(); i++) {
                char ch = line.charAt(i);
                if (ch == ';') {
                    semicolonCount++;
                }
                if (ch == ',') {
                    commaCount++;
                }
            }
            return semicolonCount > commaCount ? ';' : ',';
        }

        return ',';
    }

    private static String escapeCsv(String value) {
        if (value == null) {
            return "";
        }

        boolean requiresQuotes = value.indexOf(',') >= 0
            || value.indexOf('"') >= 0
            || value.indexOf('\n') >= 0
            || value.indexOf('\r') >= 0;

        String escaped = value.replace("\"", "\"\"");
        return requiresQuotes ? "\"" + escaped + "\"" : escaped;
    }

    private static List<List<String>> parseCsvRows(String content, char delimiter) {
        List<List<String>> rows = new ArrayList<>();
        List<String> currentRow = new ArrayList<>();
        StringBuilder currentValue = new StringBuilder();
        boolean inQuotes = false;

        for (int i = 0; i < content.length(); i++) {
            char ch = content.charAt(i);

            if (ch == '"') {
                if (inQuotes && i + 1 < content.length() && content.charAt(i + 1) == '"') {
                    currentValue.append('"');
                    i++;
                } else {
                    inQuotes = !inQuotes;
                }
                continue;
            }

            if (!inQuotes && ch == delimiter) {
                currentRow.add(currentValue.toString());
                currentValue.setLength(0);
                continue;
            }

            if (!inQuotes && (ch == '\n' || ch == '\r')) {
                if (ch == '\r' && i + 1 < content.length() && content.charAt(i + 1) == '\n') {
                    i++;
                }
                currentRow.add(currentValue.toString());
                currentValue.setLength(0);
                if (!(currentRow.size() == 1 && currentRow.get(0).isBlank())) {
                    rows.add(currentRow);
                }
                currentRow = new ArrayList<>();
                continue;
            }

            currentValue.append(ch);
        }

        currentRow.add(currentValue.toString());
        if (!(currentRow.size() == 1 && currentRow.get(0).isBlank())) {
            rows.add(currentRow);
        }

        return rows;
    }
}
