package fr.tiogars.data.settings.urlmanager.services;

import org.springframework.stereotype.Service;

import fr.tiogars.data.common.csv.CsvSupport;
import fr.tiogars.data.settings.urlmanager.models.ManagedUrl;
import fr.tiogars.data.settings.urlmanager.models.UrlCardConfig;
import fr.tiogars.data.settings.urlmanager.models.UrlManagerState;

@Service
public class UrlManagerExportCsvService {

    private final UrlManagerExportService urlManagerExportService;

    public UrlManagerExportCsvService(UrlManagerExportService urlManagerExportService) {
        this.urlManagerExportService = urlManagerExportService;
    }

    public String exportStateAsCsv() {
        UrlManagerState state = urlManagerExportService.exportState();
        StringBuilder csv = new StringBuilder();
        csv.append("type,label,url,tags,description,title,matchMode\n");

        for (ManagedUrl managedUrl : state.getUrls() != null ? state.getUrls() : java.util.List.<ManagedUrl>of()) {
            csv.append("url").append(',');
            csv.append(CsvSupport.escapeCsv(managedUrl != null ? managedUrl.getLabel() : null)).append(',');
            csv.append(CsvSupport.escapeCsv(managedUrl != null ? managedUrl.getUrl() : null)).append(',');
            csv.append(CsvSupport.escapeCsv(joinTags(managedUrl != null ? managedUrl.getTags() : null), '|')).append(',');
            csv.append(CsvSupport.escapeCsv(managedUrl != null ? managedUrl.getDescription() : null)).append(',');
            csv.append(',').append('\n');
        }

        for (UrlCardConfig cardConfig : state.getCards() != null ? state.getCards() : java.util.List.<UrlCardConfig>of()) {
            csv.append("card").append(',');
            csv.append(',').append(',');
            csv.append(CsvSupport.escapeCsv(joinTags(cardConfig != null ? cardConfig.getTags() : null), '|')).append(',');
            csv.append(',');
            csv.append(CsvSupport.escapeCsv(cardConfig != null ? cardConfig.getTitle() : null)).append(',');
            csv.append(CsvSupport.escapeCsv(cardConfig != null ? cardConfig.getMatchMode() : null));
            csv.append('\n');
        }

        return csv.toString();
    }

    private static String joinTags(java.util.List<String> tags) {
        if (tags == null || tags.isEmpty()) {
            return "";
        }
        return String.join("|", tags);
    }
}
