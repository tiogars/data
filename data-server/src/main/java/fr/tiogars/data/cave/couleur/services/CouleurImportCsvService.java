package fr.tiogars.data.cave.couleur.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import fr.tiogars.data.common.csv.CsvSupport;
import fr.tiogars.data.cave.couleur.forms.CouleurImportForm;
import fr.tiogars.data.cave.couleur.models.Couleur;
import fr.tiogars.data.cave.couleur.models.CouleurImportResult;

@Service
public class CouleurImportCsvService {
    private final CouleurImportService couleurImportService;
    public CouleurImportCsvService(CouleurImportService couleurImportService) { this.couleurImportService = couleurImportService; }
    public CouleurImportResult importCouleursFromCsv(String csvContent) {
        if (csvContent == null || csvContent.isBlank()) return couleurImportService.importCouleurs(new CouleurImportForm());
        char delimiter = CsvSupport.detectDelimiter(csvContent);
        List<List<String>> rows = CsvSupport.parseCsvRows(csvContent, delimiter);
        if (rows.isEmpty()) return couleurImportService.importCouleurs(new CouleurImportForm());
        int nameIndex = rows.getFirst().stream().map(v -> CsvSupport.normalizeHeader(v, true)).toList().indexOf("name");
        int startIndex = nameIndex >= 0 ? 1 : 0;
        if (nameIndex < 0) nameIndex = 0;
        List<Couleur> items = new ArrayList<>();
        for (int i = startIndex; i < rows.size(); i++) {
            String name = CsvSupport.valueAt(rows.get(i), nameIndex);
            if (name == null || name.isBlank()) continue;
            Couleur item = new Couleur(); item.setName(name); items.add(item);
        }
        CouleurImportForm form = new CouleurImportForm(); form.setItems(items);
        return couleurImportService.importCouleurs(form);
    }
}
