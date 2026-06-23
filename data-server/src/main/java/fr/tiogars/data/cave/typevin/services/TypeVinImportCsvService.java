package fr.tiogars.data.cave.typevin.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import fr.tiogars.data.common.csv.CsvSupport;
import fr.tiogars.data.cave.typevin.forms.TypeVinImportForm;
import fr.tiogars.data.cave.typevin.models.TypeVin;
import fr.tiogars.data.cave.typevin.models.TypeVinImportResult;

@Service
public class TypeVinImportCsvService {
    private final TypeVinImportService typeVinImportService;
    public TypeVinImportCsvService(TypeVinImportService typeVinImportService) { this.typeVinImportService = typeVinImportService; }
    public TypeVinImportResult importTypeVinsFromCsv(String csvContent) {
        if (csvContent == null || csvContent.isBlank()) return typeVinImportService.importTypeVins(new TypeVinImportForm());
        char delimiter = CsvSupport.detectDelimiter(csvContent);
        List<List<String>> rows = CsvSupport.parseCsvRows(csvContent, delimiter);
        if (rows.isEmpty()) return typeVinImportService.importTypeVins(new TypeVinImportForm());
        int nameIndex = rows.getFirst().stream().map(v -> CsvSupport.normalizeHeader(v, true)).toList().indexOf("name");
        int startIndex = nameIndex >= 0 ? 1 : 0;
        if (nameIndex < 0) nameIndex = 0;
        List<TypeVin> items = new ArrayList<>();
        for (int i = startIndex; i < rows.size(); i++) {
            String name = CsvSupport.valueAt(rows.get(i), nameIndex);
            if (name == null || name.isBlank()) continue;
            TypeVin item = new TypeVin(); item.setName(name); items.add(item);
        }
        TypeVinImportForm form = new TypeVinImportForm(); form.setItems(items);
        return typeVinImportService.importTypeVins(form);
    }
}
