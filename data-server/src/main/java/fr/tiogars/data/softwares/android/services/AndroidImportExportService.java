package fr.tiogars.data.softwares.android.services;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.tiogars.data.common.csv.CsvSupport;
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
            csv.append(CsvSupport.escapeCsv(item != null ? item.getName() : null));
            csv.append(',');
            csv.append(CsvSupport.escapeCsv(item != null ? item.getPackageName() : null));
            csv.append(',');
            csv.append(CsvSupport.escapeCsv(joinCategories(item != null ? item.getCategory() : null), '|'));
            csv.append(',');
            csv.append(CsvSupport.escapeCsv(item != null ? item.getDescription() : null));
            csv.append(',');
            csv.append(CsvSupport.escapeCsv(item != null ? item.getIcon() : null));
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

        char delimiter = CsvSupport.detectDelimiter(csvContent);
        List<List<String>> rows = CsvSupport.parseCsvRows(csvContent, delimiter);
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