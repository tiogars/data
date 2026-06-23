package fr.tiogars.data.cave.vintag.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import fr.tiogars.data.common.csv.CsvSupport;
import fr.tiogars.data.cave.vintag.forms.VinTagImportForm;
import fr.tiogars.data.cave.vintag.models.VinTag;
import fr.tiogars.data.cave.vintag.models.VinTagImportResult;

@Service
public class VinTagImportCsvService {
    private final VinTagImportService vinTagImportService;
    public VinTagImportCsvService(VinTagImportService vinTagImportService) { this.vinTagImportService = vinTagImportService; }
    public VinTagImportResult importVinTagsFromCsv(String csvContent) {
        if (csvContent == null || csvContent.isBlank()) return vinTagImportService.importVinTags(new VinTagImportForm());
        char delimiter = CsvSupport.detectDelimiter(csvContent);
        List<List<String>> rows = CsvSupport.parseCsvRows(csvContent, delimiter);
        if (rows.isEmpty()) return vinTagImportService.importVinTags(new VinTagImportForm());
        int nameIndex = rows.getFirst().stream().map(v -> CsvSupport.normalizeHeader(v, true)).toList().indexOf("name");
        int startIndex = nameIndex >= 0 ? 1 : 0;
        if (nameIndex < 0) nameIndex = 0;
        List<VinTag> items = new ArrayList<>();
        for (int i = startIndex; i < rows.size(); i++) {
            String name = CsvSupport.valueAt(rows.get(i), nameIndex);
            if (name == null || name.isBlank()) continue;
            VinTag item = new VinTag(); item.setName(name); items.add(item);
        }
        VinTagImportForm form = new VinTagImportForm(); form.setItems(items);
        return vinTagImportService.importVinTags(form);
    }
}
