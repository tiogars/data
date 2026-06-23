package fr.tiogars.data.cave.cepage.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import fr.tiogars.data.common.csv.CsvSupport;
import fr.tiogars.data.cave.cepage.forms.CepageImportForm;
import fr.tiogars.data.cave.cepage.models.Cepage;
import fr.tiogars.data.cave.cepage.models.CepageImportResult;

@Service
public class CepageImportCsvService {
    private final CepageImportService cepageImportService;
    public CepageImportCsvService(CepageImportService cepageImportService) { this.cepageImportService = cepageImportService; }
    public CepageImportResult importCepagesFromCsv(String csvContent) {
        if (csvContent == null || csvContent.isBlank()) return cepageImportService.importCepages(new CepageImportForm());
        char delimiter = CsvSupport.detectDelimiter(csvContent);
        List<List<String>> rows = CsvSupport.parseCsvRows(csvContent, delimiter);
        if (rows.isEmpty()) return cepageImportService.importCepages(new CepageImportForm());
        int nameIndex = rows.getFirst().stream().map(v -> CsvSupport.normalizeHeader(v, true)).toList().indexOf("name");
        int startIndex = nameIndex >= 0 ? 1 : 0;
        if (nameIndex < 0) nameIndex = 0;
        List<Cepage> items = new ArrayList<>();
        for (int i = startIndex; i < rows.size(); i++) {
            String name = CsvSupport.valueAt(rows.get(i), nameIndex);
            if (name == null || name.isBlank()) continue;
            Cepage item = new Cepage(); item.setName(name); items.add(item);
        }
        CepageImportForm form = new CepageImportForm(); form.setItems(items);
        return cepageImportService.importCepages(form);
    }
}
