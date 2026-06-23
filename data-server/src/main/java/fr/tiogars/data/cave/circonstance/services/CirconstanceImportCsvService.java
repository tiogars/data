package fr.tiogars.data.cave.circonstance.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import fr.tiogars.data.common.csv.CsvSupport;
import fr.tiogars.data.cave.circonstance.forms.CirconstanceImportForm;
import fr.tiogars.data.cave.circonstance.models.Circonstance;
import fr.tiogars.data.cave.circonstance.models.CirconstanceImportResult;

@Service
public class CirconstanceImportCsvService {
    private final CirconstanceImportService circonstanceImportService;
    public CirconstanceImportCsvService(CirconstanceImportService circonstanceImportService) { this.circonstanceImportService = circonstanceImportService; }
    public CirconstanceImportResult importCirconstancesFromCsv(String csvContent) {
        if (csvContent == null || csvContent.isBlank()) return circonstanceImportService.importCirconstances(new CirconstanceImportForm());
        char delimiter = CsvSupport.detectDelimiter(csvContent);
        List<List<String>> rows = CsvSupport.parseCsvRows(csvContent, delimiter);
        if (rows.isEmpty()) return circonstanceImportService.importCirconstances(new CirconstanceImportForm());
        int nameIndex = rows.getFirst().stream().map(v -> CsvSupport.normalizeHeader(v, true)).toList().indexOf("name");
        int startIndex = nameIndex >= 0 ? 1 : 0;
        if (nameIndex < 0) nameIndex = 0;
        List<Circonstance> items = new ArrayList<>();
        for (int i = startIndex; i < rows.size(); i++) {
            String name = CsvSupport.valueAt(rows.get(i), nameIndex);
            if (name == null || name.isBlank()) continue;
            Circonstance item = new Circonstance(); item.setName(name); items.add(item);
        }
        CirconstanceImportForm form = new CirconstanceImportForm(); form.setItems(items);
        return circonstanceImportService.importCirconstances(form);
    }
}
