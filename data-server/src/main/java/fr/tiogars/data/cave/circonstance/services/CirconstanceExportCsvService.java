package fr.tiogars.data.cave.circonstance.services;

import org.springframework.stereotype.Service;
import fr.tiogars.data.common.csv.CsvSupport;
import fr.tiogars.data.cave.circonstance.models.Circonstance;

@Service
public class CirconstanceExportCsvService {
    private final CirconstanceExportService circonstanceExportService;
    public CirconstanceExportCsvService(CirconstanceExportService circonstanceExportService) { this.circonstanceExportService = circonstanceExportService; }
    public String exportCirconstancesAsCsv() {
        StringBuilder csv = new StringBuilder();
        csv.append("name\n");
        for (Circonstance item : circonstanceExportService.exportCirconstances().getItems()) {
            csv.append(CsvSupport.escapeCsv(item != null ? item.getName() : null)).append('\n');
        }
        return csv.toString();
    }
}
