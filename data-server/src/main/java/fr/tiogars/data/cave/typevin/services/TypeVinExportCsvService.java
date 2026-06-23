package fr.tiogars.data.cave.typevin.services;

import org.springframework.stereotype.Service;
import fr.tiogars.data.common.csv.CsvSupport;
import fr.tiogars.data.cave.typevin.models.TypeVin;

@Service
public class TypeVinExportCsvService {
    private final TypeVinExportService typeVinExportService;
    public TypeVinExportCsvService(TypeVinExportService typeVinExportService) { this.typeVinExportService = typeVinExportService; }
    public String exportTypeVinsAsCsv() {
        StringBuilder csv = new StringBuilder();
        csv.append("name\n");
        for (TypeVin item : typeVinExportService.exportTypeVins().getItems()) {
            csv.append(CsvSupport.escapeCsv(item != null ? item.getName() : null)).append('\n');
        }
        return csv.toString();
    }
}
