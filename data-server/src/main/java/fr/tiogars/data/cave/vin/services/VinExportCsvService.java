package fr.tiogars.data.cave.vin.services;

import org.springframework.stereotype.Service;

import fr.tiogars.data.cave.vin.models.Vin;
import fr.tiogars.data.common.csv.CsvSupport;

@Service
public class VinExportCsvService {

    private final VinExportService vinExportService;

    public VinExportCsvService(VinExportService vinExportService) {
        this.vinExportService = vinExportService;
    }

    public String exportVinsAsCsv() {
        StringBuilder csv = new StringBuilder();
        csv.append("id,annee,region,commune,commentaires\n");
        for (Vin item : vinExportService.exportVins().getItems()) {
            csv.append(CsvSupport.escapeCsv(item != null ? item.getId() : null));
            csv.append(',');
            csv.append(CsvSupport.escapeCsv(item != null && item.getAnnee() != null ? item.getAnnee().toString() : null));
            csv.append(',');
            csv.append(CsvSupport.escapeCsv(item != null ? item.getRegion() : null));
            csv.append(',');
            csv.append(CsvSupport.escapeCsv(item != null ? item.getCommune() : null));
            csv.append(',');
            csv.append(CsvSupport.escapeCsv(item != null ? item.getCommentaires() : null));
            csv.append('\n');
        }
        return csv.toString();
    }
}
