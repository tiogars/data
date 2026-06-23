package fr.tiogars.data.cave.couleur.services;

import org.springframework.stereotype.Service;
import fr.tiogars.data.common.csv.CsvSupport;
import fr.tiogars.data.cave.couleur.models.Couleur;

@Service
public class CouleurExportCsvService {
    private final CouleurExportService couleurExportService;
    public CouleurExportCsvService(CouleurExportService couleurExportService) { this.couleurExportService = couleurExportService; }
    public String exportCouleursAsCsv() {
        StringBuilder csv = new StringBuilder();
        csv.append("name\n");
        for (Couleur item : couleurExportService.exportCouleurs().getItems()) {
            csv.append(CsvSupport.escapeCsv(item != null ? item.getName() : null)).append('\n');
        }
        return csv.toString();
    }
}
