package fr.tiogars.data.cave.vintag.services;

import org.springframework.stereotype.Service;
import fr.tiogars.data.common.csv.CsvSupport;
import fr.tiogars.data.cave.vintag.models.VinTag;

@Service
public class VinTagExportCsvService {
    private final VinTagExportService vinTagExportService;
    public VinTagExportCsvService(VinTagExportService vinTagExportService) { this.vinTagExportService = vinTagExportService; }
    public String exportVinTagsAsCsv() {
        StringBuilder csv = new StringBuilder();
        csv.append("name\n");
        for (VinTag item : vinTagExportService.exportVinTags().getItems()) {
            csv.append(CsvSupport.escapeCsv(item != null ? item.getName() : null)).append('\n');
        }
        return csv.toString();
    }
}
