package fr.tiogars.data.products.brand.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import fr.tiogars.data.common.csv.CsvSupport;
import fr.tiogars.data.products.brand.forms.BrandImportForm;
import fr.tiogars.data.products.brand.models.Brand;
import fr.tiogars.data.products.brand.models.BrandImportResult;

@Service
public class BrandImportCsvService {

    private final BrandImportService brandImportService;

    public BrandImportCsvService(BrandImportService brandImportService) {
        this.brandImportService = brandImportService;
    }

    public BrandImportResult importBrandsFromCsv(String csvContent) {
        if (csvContent == null || csvContent.isBlank()) {
            return brandImportService.importBrands(new BrandImportForm());
        }

        char delimiter = CsvSupport.detectDelimiter(csvContent);
        List<List<String>> rows = CsvSupport.parseCsvRows(csvContent, delimiter);
        if (rows.isEmpty()) {
            return brandImportService.importBrands(new BrandImportForm());
        }

        CsvColumnMapping mapping = resolveColumnMapping(rows.getFirst());
        int startIndex = mapping.hasHeader() ? 1 : 0;
        List<Brand> items = new ArrayList<>();

        for (int i = startIndex; i < rows.size(); i++) {
            List<String> row = rows.get(i);
            if (row.isEmpty()) {
                continue;
            }

            String name = CsvSupport.valueAt(row, mapping.nameIndex());
            String description = CsvSupport.valueAt(row, mapping.descriptionIndex());
            if ((name == null || name.isBlank()) && (description == null || description.isBlank())) {
                continue;
            }

            Brand brand = new Brand();
            brand.setName(name);
            brand.setDescription(description);
            items.add(brand);
        }

        BrandImportForm form = new BrandImportForm();
        form.setItems(items);
        return brandImportService.importBrands(form);
    }

    private record CsvColumnMapping(boolean hasHeader, int nameIndex, int descriptionIndex) { }

    private static CsvColumnMapping resolveColumnMapping(List<String> firstRow) {
        if (firstRow == null || firstRow.isEmpty()) {
            return new CsvColumnMapping(false, 0, 1);
        }

        int nameIndex = -1;
        int descriptionIndex = -1;

        for (int i = 0; i < firstRow.size(); i++) {
            String normalizedHeader = CsvSupport.normalizeHeader(firstRow.get(i));
            if ("name".equals(normalizedHeader)) {
                nameIndex = i;
            }
            if ("description".equals(normalizedHeader)) {
                descriptionIndex = i;
            }
        }

        if (nameIndex >= 0 || descriptionIndex >= 0) {
            return new CsvColumnMapping(true, nameIndex >= 0 ? nameIndex : 0, descriptionIndex >= 0 ? descriptionIndex : 1);
        }

        return new CsvColumnMapping(false, 0, 1);
    }
}
