package fr.tiogars.data.cave.cepage.services;

import org.springframework.stereotype.Service;
import fr.tiogars.data.common.csv.CsvSupport;
import fr.tiogars.data.cave.cepage.models.Cepage;

@Service
public class CepageExportCsvService {
    private final CepageExportService cepageExportService;
    public CepageExportCsvService(CepageExportService cepageExportService) { this.cepageExportService = cepageExportService; }
    public String exportCepagesAsCsv() {
        StringBuilder csv = new StringBuilder();
        csv.append("name\n");
        for (Cepage item : cepageExportService.exportCepages().getItems()) {
            csv.append(CsvSupport.escapeCsv(item != null ? item.getName() : null)).append('\n');
        }
        return csv.toString();
    }
}
