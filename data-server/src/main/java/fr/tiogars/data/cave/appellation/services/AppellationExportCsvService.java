package fr.tiogars.data.cave.appellation.services;

import org.springframework.stereotype.Service;
import fr.tiogars.data.common.csv.CsvSupport;
import fr.tiogars.data.cave.appellation.models.Appellation;

@Service
public class AppellationExportCsvService {
    private final AppellationExportService appellationExportService;
    public AppellationExportCsvService(AppellationExportService appellationExportService) { this.appellationExportService = appellationExportService; }
    public String exportAppellationsAsCsv() {
        StringBuilder csv = new StringBuilder();
        csv.append("name\n");
        for (Appellation item : appellationExportService.exportAppellations().getItems()) {
            csv.append(CsvSupport.escapeCsv(item != null ? item.getName() : null)).append('\n');
        }
        return csv.toString();
    }
}
