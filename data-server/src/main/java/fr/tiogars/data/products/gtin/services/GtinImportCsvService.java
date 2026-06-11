package fr.tiogars.data.products.gtin.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import fr.tiogars.data.common.csv.CsvSupport;
import fr.tiogars.data.products.gtin.models.Gtin;
import fr.tiogars.data.products.gtin.models.GtinImportResult;

@Service
public class GtinImportCsvService {

    private final GtinImportService gtinImportService;

    public GtinImportCsvService(GtinImportService gtinImportService) {
        this.gtinImportService = gtinImportService;
    }

    public GtinImportResult importGtinsFromCsv(String csvContent) {
        if (csvContent == null || csvContent.isBlank()) {
            return gtinImportService.importGtins(List.of());
        }

        char delimiter = CsvSupport.detectDelimiter(csvContent);
        List<List<String>> rows = CsvSupport.parseCsvRows(csvContent, delimiter);
        if (rows.isEmpty()) {
            return gtinImportService.importGtins(List.of());
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

        return gtinImportService.importGtins(items);
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
