package fr.tiogars.data.dev.model.services;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

import org.springframework.stereotype.Service;

import fr.tiogars.data.common.csv.CsvSupport;
import fr.tiogars.data.dev.model.models.Model;
import fr.tiogars.data.dev.model.models.ModelAttribute;
import fr.tiogars.data.dev.model.models.ModelImportResult;

@Service
public class ModelImportCsvService {

    private final ModelImportService modelImportService;

    public ModelImportCsvService(ModelImportService modelImportService) {
        this.modelImportService = modelImportService;
    }

    public ModelImportResult importModelsFromCsv(String csvContent) {
        if (csvContent == null || csvContent.isBlank()) {
            return modelImportService.importModels(List.of());
        }

        char delimiter = CsvSupport.detectDelimiter(csvContent);
        List<List<String>> rows = CsvSupport.parseCsvRows(csvContent, delimiter);
        if (rows.isEmpty()) {
            return modelImportService.importModels(List.of());
        }

        CsvColumnMapping mapping = resolveColumnMapping(rows.getFirst());
        int startIndex = mapping.hasHeader() ? 1 : 0;
        List<Model> items = new ArrayList<>();

        for (int i = startIndex; i < rows.size(); i++) {
            List<String> row = rows.get(i);
            if (row.isEmpty()) {
                continue;
            }

            String name = CsvSupport.valueAt(row, mapping.nameIndex());
            String description = CsvSupport.valueAt(row, mapping.descriptionIndex());
            String attributes = CsvSupport.valueAt(row, mapping.attributesIndex());
            if ((name == null || name.isBlank()) && (description == null || description.isBlank()) && (attributes == null || attributes.isBlank())) {
                continue;
            }

            Model model = new Model();
            model.setName(name);
            model.setDescription(description);
            model.setModelAttributes(parseAttributes(attributes));
            items.add(model);
        }

        return modelImportService.importModels(items);
    }

    private record CsvColumnMapping(boolean hasHeader, int nameIndex, int descriptionIndex, int attributesIndex) { }

    private static CsvColumnMapping resolveColumnMapping(List<String> firstRow) {
        if (firstRow == null || firstRow.isEmpty()) {
            return new CsvColumnMapping(false, 0, 1, 2);
        }

        int nameIndex = -1;
        int descriptionIndex = -1;
        int attributesIndex = -1;

        for (int i = 0; i < firstRow.size(); i++) {
            String normalizedHeader = CsvSupport.normalizeHeader(firstRow.get(i), true);
            if ("name".equals(normalizedHeader)) {
                nameIndex = i;
            }
            if ("description".equals(normalizedHeader)) {
                descriptionIndex = i;
            }
            if ("attributes".equals(normalizedHeader) || "modelattributes".equals(normalizedHeader)) {
                attributesIndex = i;
            }
        }

        if (nameIndex >= 0 || descriptionIndex >= 0 || attributesIndex >= 0) {
            return new CsvColumnMapping(
                true,
                nameIndex >= 0 ? nameIndex : 0,
                descriptionIndex >= 0 ? descriptionIndex : 1,
                attributesIndex >= 0 ? attributesIndex : 2
            );
        }

        return new CsvColumnMapping(false, 0, 1, 2);
    }

    private static List<ModelAttribute> parseAttributes(String value) {
        if (value == null || value.isBlank()) {
            return new ArrayList<>();
        }

        LinkedHashSet<String> uniquePairs = new LinkedHashSet<>(java.util.Arrays.asList(value.split("\\|")));
        List<ModelAttribute> attributes = new ArrayList<>();

        for (String pair : uniquePairs) {
            String entry = pair != null ? pair.trim() : "";
            if (entry.isBlank()) {
                continue;
            }

            String[] parts = entry.split("::", 2);
            ModelAttribute attribute = new ModelAttribute();
            attribute.setName(parts.length > 0 ? parts[0].trim() : null);
            attribute.setDescription(parts.length > 1 ? parts[1].trim() : null);
            attributes.add(attribute);
        }

        return attributes;
    }
}
