package fr.tiogars.data.softwares.android.services;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

import org.springframework.stereotype.Service;

import fr.tiogars.data.common.csv.CsvSupport;
import fr.tiogars.data.softwares.android.models.Android;
import fr.tiogars.data.softwares.android.models.AndroidImportResult;

@Service
public class AndroidImportCsvService {

    private final AndroidImportService androidImportService;

    public AndroidImportCsvService(AndroidImportService androidImportService) {
        this.androidImportService = androidImportService;
    }

    public AndroidImportResult importAndroidsFromCsv(String csvContent) {
        if (csvContent == null || csvContent.isBlank()) {
            return androidImportService.importAndroids(List.of());
        }

        char delimiter = CsvSupport.detectDelimiter(csvContent);
        List<List<String>> rows = CsvSupport.parseCsvRows(csvContent, delimiter);
        if (rows.isEmpty()) {
            return androidImportService.importAndroids(List.of());
        }

        CsvColumnMapping mapping = resolveColumnMapping(rows.getFirst());
        int startIndex = mapping.hasHeader() ? 1 : 0;
        List<Android> items = new ArrayList<>();

        for (int i = startIndex; i < rows.size(); i++) {
            List<String> row = rows.get(i);
            if (row.isEmpty()) {
                continue;
            }

            String name = CsvSupport.valueAt(row, mapping.nameIndex());
            String packageName = CsvSupport.valueAt(row, mapping.packageNameIndex());
            String category = CsvSupport.valueAt(row, mapping.categoryIndex());
            String description = CsvSupport.valueAt(row, mapping.descriptionIndex());
            String icon = CsvSupport.valueAt(row, mapping.iconIndex());
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

        return androidImportService.importAndroids(items);
    }

    private record CsvColumnMapping(boolean hasHeader, int nameIndex, int packageNameIndex, int categoryIndex, int descriptionIndex, int iconIndex) {
    }

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
            String normalizedHeader = CsvSupport.normalizeHeader(firstRow.get(i), true);
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

    private static List<String> splitCategories(String value) {
        if (value == null || value.isBlank()) {
            return new ArrayList<>();
        }

        return new ArrayList<>(java.util.Arrays.stream(value.split("[|;,]"))
            .map(AndroidImportCsvService::trim)
            .filter(item -> !item.isBlank())
            .collect(java.util.stream.Collectors.toCollection(LinkedHashSet::new)));
    }

    private static String trim(@org.jspecify.annotations.NonNull String value) {
        return value.trim();
    }
}
