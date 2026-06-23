package fr.tiogars.data.cave.vinnom.services;

import org.springframework.stereotype.Service;

import fr.tiogars.data.common.csv.CsvSupport;
import fr.tiogars.data.cave.vinnom.models.VinNom;

@Service
public class VinNomExportCsvService {
    private final VinNomExportService vinNomExportService;
    public VinNomExportCsvService(VinNomExportService vinNomExportService) { this.vinNomExportService = vinNomExportService; }
    public String exportVinNomsAsCsv() {
        StringBuilder csv = new StringBuilder();
        csv.append("name,maison_id\n");
        for (VinNom item : vinNomExportService.exportVinNoms().getItems()) {
            csv.append(CsvSupport.escapeCsv(item != null ? item.getName() : null)).append(',').append(CsvSupport.escapeCsv(item != null ? item.getMaisonId() : null)).append('\n');
        }
        return csv.toString();
    }
}
