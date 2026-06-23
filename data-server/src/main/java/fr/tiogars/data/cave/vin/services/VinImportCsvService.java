package fr.tiogars.data.cave.vin.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import fr.tiogars.data.cave.vin.forms.VinImportForm;
import fr.tiogars.data.cave.vin.models.Vin;
import fr.tiogars.data.cave.vin.models.VinImportResult;
import fr.tiogars.data.common.csv.CsvSupport;

@Service
public class VinImportCsvService {

    private final VinImportService vinImportService;

    public VinImportCsvService(VinImportService vinImportService) {
        this.vinImportService = vinImportService;
    }

    public VinImportResult importVinsFromCsv(String csvContent) {
        if (csvContent == null || csvContent.isBlank()) {
            return vinImportService.importVins(new VinImportForm());
        }

        char delimiter = CsvSupport.detectDelimiter(csvContent);
        List<List<String>> rows = CsvSupport.parseCsvRows(csvContent, delimiter);
        if (rows.isEmpty()) {
            return vinImportService.importVins(new VinImportForm());
        }

        CsvColumnMapping mapping = resolveColumnMapping(rows.getFirst());
        int startIndex = mapping.hasHeader() ? 1 : 0;
        List<Vin> items = new ArrayList<>();

        for (int i = startIndex; i < rows.size(); i++) {
            List<String> row = rows.get(i);
            if (row.isEmpty()) {
                continue;
            }

            String annee = CsvSupport.valueAt(row, mapping.anneeIndex());
            String region = CsvSupport.valueAt(row, mapping.regionIndex());
            String commune = CsvSupport.valueAt(row, mapping.communeIndex());
            String commentaires = CsvSupport.valueAt(row, mapping.commentairesIndex());
            if ((annee == null || annee.isBlank())
                && (region == null || region.isBlank())
                && (commune == null || commune.isBlank())
                && (commentaires == null || commentaires.isBlank())) {
                continue;
            }

            Vin item = new Vin();
            item.setAnnee(parseInteger(annee));
            item.setRegion(region);
            item.setCommune(commune);
            item.setCommentaires(commentaires);
            items.add(item);
        }

        VinImportForm form = new VinImportForm();
        form.setItems(items);
        return vinImportService.importVins(form);
    }

    private record CsvColumnMapping(boolean hasHeader, int anneeIndex, int regionIndex, int communeIndex, int commentairesIndex) {
    }

    private static CsvColumnMapping resolveColumnMapping(List<String> firstRow) {
        if (firstRow == null || firstRow.isEmpty()) {
            return new CsvColumnMapping(false, 0, 1, 2, 3);
        }

        int anneeIndex = -1;
        int regionIndex = -1;
        int communeIndex = -1;
        int commentairesIndex = -1;

        for (int i = 0; i < firstRow.size(); i++) {
            String normalizedHeader = CsvSupport.normalizeHeader(firstRow.get(i), true);
            if ("annee".equals(normalizedHeader) || "millesime".equals(normalizedHeader)) {
                anneeIndex = i;
            }
            if ("region".equals(normalizedHeader)) {
                regionIndex = i;
            }
            if ("commune".equals(normalizedHeader)) {
                communeIndex = i;
            }
            if ("commentaires".equals(normalizedHeader) || "commentaire".equals(normalizedHeader)) {
                commentairesIndex = i;
            }
        }

        if (anneeIndex >= 0 || regionIndex >= 0 || communeIndex >= 0 || commentairesIndex >= 0) {
            return new CsvColumnMapping(
                true,
                anneeIndex >= 0 ? anneeIndex : 0,
                regionIndex >= 0 ? regionIndex : 1,
                communeIndex >= 0 ? communeIndex : 2,
                commentairesIndex >= 0 ? commentairesIndex : 3
            );
        }

        return new CsvColumnMapping(false, 0, 1, 2, 3);
    }

    private static Integer parseInteger(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        try {
            return Integer.valueOf(value.trim());
        } catch (NumberFormatException ex) {
            return null;
        }
    }
}
