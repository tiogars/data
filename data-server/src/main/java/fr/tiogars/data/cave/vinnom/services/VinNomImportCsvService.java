package fr.tiogars.data.cave.vinnom.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import fr.tiogars.data.common.csv.CsvSupport;
import fr.tiogars.data.cave.vinnom.forms.VinNomImportForm;
import fr.tiogars.data.cave.vinnom.models.VinNom;
import fr.tiogars.data.cave.vinnom.models.VinNomImportResult;

@Service
public class VinNomImportCsvService {
    private final VinNomImportService vinNomImportService;
    public VinNomImportCsvService(VinNomImportService vinNomImportService) { this.vinNomImportService = vinNomImportService; }
    public VinNomImportResult importVinNomsFromCsv(String csvContent) {
        if (csvContent == null || csvContent.isBlank()) return vinNomImportService.importVinNoms(new VinNomImportForm());
        char delimiter = CsvSupport.detectDelimiter(csvContent);
        List<List<String>> rows = CsvSupport.parseCsvRows(csvContent, delimiter);
        if (rows.isEmpty()) return vinNomImportService.importVinNoms(new VinNomImportForm());
        int nameIndex = 0;
        int maisonIdIndex = 1;
        var headers = rows.getFirst().stream().map(v -> CsvSupport.normalizeHeader(v, true)).toList();
        if (headers.contains("name") || headers.contains("maisonid")) {
            nameIndex = Math.max(headers.indexOf("name"), 0);
            maisonIdIndex = headers.indexOf("maisonid");
            if (maisonIdIndex < 0) maisonIdIndex = 1;
            rows = rows.subList(1, rows.size());
        }
        List<VinNom> items = new ArrayList<>();
        for (List<String> row : rows) {
            String name = CsvSupport.valueAt(row, nameIndex);
            String maisonId = CsvSupport.valueAt(row, maisonIdIndex);
            if ((name == null || name.isBlank()) && (maisonId == null || maisonId.isBlank())) continue;
            VinNom item = new VinNom();
            item.setName(name);
            item.setMaisonId(maisonId);
            items.add(item);
        }
        VinNomImportForm form = new VinNomImportForm();
        form.setItems(items);
        return vinNomImportService.importVinNoms(form);
    }
}
