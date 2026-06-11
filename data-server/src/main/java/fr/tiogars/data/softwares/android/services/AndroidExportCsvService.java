package fr.tiogars.data.softwares.android.services;

import org.springframework.stereotype.Service;

import fr.tiogars.data.common.csv.CsvSupport;
import fr.tiogars.data.softwares.android.models.Android;

@Service
public class AndroidExportCsvService {

    private final AndroidExportService androidExportService;

    public AndroidExportCsvService(AndroidExportService androidExportService) {
        this.androidExportService = androidExportService;
    }

    public String exportAndroidsAsCsv() {
        StringBuilder csv = new StringBuilder();
        csv.append("name,packageName,category,description,icon\n");

        for (Android item : androidExportService.exportAndroids().getItems()) {
            csv.append(CsvSupport.escapeCsv(item != null ? item.getName() : null));
            csv.append(',');
            csv.append(CsvSupport.escapeCsv(item != null ? item.getPackageName() : null));
            csv.append(',');
            csv.append(CsvSupport.escapeCsv(joinCategories(item != null ? item.getCategory() : null), '|'));
            csv.append(',');
            csv.append(CsvSupport.escapeCsv(item != null ? item.getDescription() : null));
            csv.append(',');
            csv.append(CsvSupport.escapeCsv(item != null ? item.getIcon() : null));
            csv.append('\n');
        }

        return csv.toString();
    }

    private static String joinCategories(java.util.List<String> categories) {
        if (categories == null || categories.isEmpty()) {
            return "";
        }

        return String.join("|", categories);
    }
}
