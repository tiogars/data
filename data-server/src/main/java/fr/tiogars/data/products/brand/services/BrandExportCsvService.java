package fr.tiogars.data.products.brand.services;

import org.springframework.stereotype.Service;

import fr.tiogars.data.common.csv.CsvSupport;
import fr.tiogars.data.products.brand.models.Brand;

@Service
public class BrandExportCsvService {

    private final BrandExportService brandExportService;

    public BrandExportCsvService(BrandExportService brandExportService) {
        this.brandExportService = brandExportService;
    }

    public String exportBrandsAsCsv() {
        StringBuilder csv = new StringBuilder();
        csv.append("name,description\n");

        for (Brand item : brandExportService.exportBrands().getItems()) {
            csv.append(CsvSupport.escapeCsv(item != null ? item.getName() : null));
            csv.append(',');
            csv.append(CsvSupport.escapeCsv(item != null ? item.getDescription() : null));
            csv.append('\n');
        }

        return csv.toString();
    }
}
