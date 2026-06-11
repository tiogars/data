package fr.tiogars.data.products.gtin.services;

import org.springframework.stereotype.Service;

import fr.tiogars.data.common.csv.CsvSupport;
import fr.tiogars.data.products.gtin.models.Gtin;

@Service
public class GtinExportCsvService {

    private final GtinExportService gtinExportService;

    public GtinExportCsvService(GtinExportService gtinExportService) {
        this.gtinExportService = gtinExportService;
    }

    public String exportGtinsAsCsv() {
        StringBuilder csv = new StringBuilder();
        csv.append("code,description\n");

        for (Gtin item : gtinExportService.exportGtins().getItems()) {
            csv.append(CsvSupport.escapeCsv(item != null ? item.getCode() : null));
            csv.append(',');
            csv.append(CsvSupport.escapeCsv(item != null ? item.getDescription() : null));
            csv.append('\n');
        }

        return csv.toString();
    }
}
