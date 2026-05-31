package fr.tiogars.data.softwares.android.services;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.tiogars.data.softwares.android.entities.AndroidEntity;
import fr.tiogars.data.softwares.android.models.Android;
import fr.tiogars.data.softwares.android.models.AndroidImportResult;
import fr.tiogars.data.softwares.android.models.AndroidListResponse;
import fr.tiogars.data.softwares.android.repositories.AndroidRepository;

@Service
public class AndroidImportExportService {

    private final AndroidRepository androidRepository;

    public AndroidImportExportService(AndroidRepository androidRepository) {
        this.androidRepository = androidRepository;
    }

    public AndroidListResponse exportAndroids() {
        List<Android> items = androidRepository.findAllByOrderByNameAsc().stream()
            .map(AndroidModelMapper::toModel)
            .toList();

        return new AndroidListResponse(items, items.size());
    }

    public String exportAndroidsAsCsv() {
        List<Android> items = exportAndroids().getItems();
        StringBuilder csv = new StringBuilder();
        csv.append("name,packageName,category,description,icon\n");

        for (Android item : items) {
            csv.append(escapeCsv(item != null ? item.getName() : null));
            csv.append(',');
            csv.append(escapeCsv(item != null ? item.getPackageName() : null));
            csv.append(',');
            csv.append(escapeCsv(joinCategories(item != null ? item.getCategory() : null)));
            csv.append(',');
            csv.append(escapeCsv(item != null ? item.getDescription() : null));
            csv.append(',');
            csv.append(escapeCsv(item != null ? item.getIcon() : null));
            csv.append('\n');
        }

        return csv.toString();
    }

    @Transactional
    public AndroidImportResult importAndroids(List<Android> items) {
        List<Android> rawItems = items != null ? items : List.of();

        Set<String> seenPackageNames = new HashSet<>();
        List<Android> uniqueItems = new ArrayList<>();
        List<String> duplicatePackageNames = new ArrayList<>();

        for (Android item : rawItems) {
            if (item == null) {
                continue;
            }

            String normalizedName = AndroidCreationService.requireText(item.getName(), "Le nom de l'application Android est obligatoire.");
            String normalizedPackageName = AndroidCreationService.requireText(item.getPackageName(), "Le package Android est obligatoire.");
            item.setName(normalizedName);
            item.setPackageName(normalizedPackageName);
            item.setCategory(AndroidCreationService.normalizeCategories(item.getCategory()));
            item.setDescription(AndroidCreationService.normalizeNullableText(item.getDescription()));
            item.setIcon(AndroidCreationService.normalizeNullableText(item.getIcon()));

            if (seenPackageNames.add(normalizedPackageName)) {
                uniqueItems.add(item);
            } else if (!duplicatePackageNames.contains(normalizedPackageName)) {
                duplicatePackageNames.add(normalizedPackageName);
            }
        }

        androidRepository.deleteAllInBatch();

        List<AndroidEntity> entities = uniqueItems.stream()
            .map(item -> {
                AndroidEntity entity = new AndroidEntity();
                AndroidCreationService.applyValues(entity, item.getName(), item.getPackageName(), item.getCategory(), item.getDescription(), item.getIcon());
                return entity;
            })
            .toList();

        androidRepository.saveAll(entities);

        List<Android> imported = androidRepository.findAllByOrderByNameAsc().stream()
            .map(AndroidModelMapper::toModel)
            .toList();

        return new AndroidImportResult(imported, duplicatePackageNames);
    }

    @Transactional
    public AndroidImportResult importAndroidsFromCsv(String csvContent) {
        if (csvContent == null || csvContent.isBlank()) {
            return importAndroids(List.of());
        }

        char delimiter = detectDelimiter(csvContent);
        List<List<String>> rows = parseCsvRows(csvContent, delimiter);
        if (rows.isEmpty()) {
            return importAndroids(List.of());
        }

        CsvColumnMapping mapping = resolveColumnMapping(rows.getFirst());
        int startIndex = mapping.hasHeader() ? 1 : 0;
        List<Android> items = new ArrayList<>();

        for (int i = startIndex; i < rows.size(); i++) {
            List<String> row = rows.get(i);
            if (row.isEmpty()) {
                continue;
            }

            String name = valueAt(row, mapping.nameIndex());
            String packageName = valueAt(row, mapping.packageNameIndex());
            String category = valueAt(row, mapping.categoryIndex());
            String description = valueAt(row, mapping.descriptionIndex());
            String icon = valueAt(row, mapping.iconIndex());
            if ((name == null || name.isBlank()) && (packageName == null || packageName.isBlank())) {
                continue;
            }

            Android android = new Android();
            android.setName(name);
            android.setPackageName(packageName);
            android.setCategory(splitCategories(category));
            android.setDescription(description);
            android.setIcon(icon);
            items.add(android);
        }

        return importAndroids(items);
    }

    private record CsvColumnMapping(boolean hasHeader, int nameIndex, int packageNameIndex, int categoryIndex, int descriptionIndex, int iconIndex) { }

    private static CsvColumnMapping resolveColumnMapping(List<String> firstRow) {
        if (firstRow == null || firstRow.isEmpty()) {
            return new CsvColumnMapping(false, 0, 1, 2, 3, 4);
        }

        int nameIndex = -1;
        int packageNameIndex = -1;
        int categoryIndex = -1;
        int descriptionIndex = -1;
        int iconIndex = -1;

        for (int i = 0; i < firstRow.size(); i++) {
            String normalizedHeader = normalizeHeader(firstRow.get(i));
            if ("name".equals(normalizedHeader)) {
                nameIndex = i;
            }
            if ("packagename".equals(normalizedHeader)) {
                packageNameIndex = i;
            }
            if ("category".equals(normalizedHeader)) {
                categoryIndex = i;
            }
            if ("description".equals(normalizedHeader)) {
                descriptionIndex = i;
            }
            if ("icon".equals(normalizedHeader)) {
                iconIndex = i;
            }
        }

        if (nameIndex >= 0 || packageNameIndex >= 0 || categoryIndex >= 0 || descriptionIndex >= 0 || iconIndex >= 0) {
            return new CsvColumnMapping(
                true,
                nameIndex >= 0 ? nameIndex : 0,
                packageNameIndex >= 0 ? packageNameIndex : 1,
                categoryIndex >= 0 ? categoryIndex : 2,
                descriptionIndex >= 0 ? descriptionIndex : 3,
                iconIndex >= 0 ? iconIndex : 4
            );
        }

        return new CsvColumnMapping(false, 0, 1, 2, 3, 4);
    }

    private static String normalizeHeader(String value) {
        if (value == null) {
            return "";
        }
        return value
            .replace("\uFEFF", "")
            .trim()
            .toLowerCase(Locale.ROOT)
            .replaceAll("[^a-z0-9]", "");
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
            || value.indexOf('|') >= 0
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

    private static List<String> splitCategories(String value) {
        if (value == null || value.isBlank()) {
            return new ArrayList<>();
        }

        return new ArrayList<>(java.util.Arrays.stream(value.split("[|;,]"))
            .map(String::trim)
            .filter(item -> !item.isBlank())
            .collect(java.util.stream.Collectors.toCollection(LinkedHashSet::new)));
    }

    private static String joinCategories(List<String> categories) {
        if (categories == null || categories.isEmpty()) {
            return "";
        }

        return String.join("|", categories);
    }
}