package fr.tiogars.data.cave.appellation.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import fr.tiogars.data.common.csv.CsvSupport;
import fr.tiogars.data.cave.appellation.forms.AppellationImportForm;
import fr.tiogars.data.cave.appellation.models.Appellation;
import fr.tiogars.data.cave.appellation.models.AppellationImportResult;

@Service
public class AppellationImportCsvService {
    private final AppellationImportService appellationImportService;
    public AppellationImportCsvService(AppellationImportService appellationImportService) { this.appellationImportService = appellationImportService; }
    public AppellationImportResult importAppellationsFromCsv(String csvContent) {
        if (csvContent == null || csvContent.isBlank()) return appellationImportService.importAppellations(new AppellationImportForm());
        char delimiter = CsvSupport.detectDelimiter(csvContent);
        List<List<String>> rows = CsvSupport.parseCsvRows(csvContent, delimiter);
        if (rows.isEmpty()) return appellationImportService.importAppellations(new AppellationImportForm());
        int nameIndex = rows.getFirst().stream().map(v -> CsvSupport.normalizeHeader(v, true)).toList().indexOf("name");
        int startIndex = nameIndex >= 0 ? 1 : 0;
        if (nameIndex < 0) nameIndex = 0;
        List<Appellation> items = new ArrayList<>();
        for (int i = startIndex; i < rows.size(); i++) {
            String name = CsvSupport.valueAt(rows.get(i), nameIndex);
            if (name == null || name.isBlank()) continue;
            Appellation item = new Appellation(); item.setName(name); items.add(item);
        }
        AppellationImportForm form = new AppellationImportForm(); form.setItems(items);
        return appellationImportService.importAppellations(form);
    }
}
