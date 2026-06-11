package fr.tiogars.data.games.brick.services;

import org.springframework.stereotype.Service;

import fr.tiogars.data.common.csv.CsvSupport;
import fr.tiogars.data.games.brick.models.Brick;
import fr.tiogars.data.games.brick.models.BrickState;
import fr.tiogars.data.games.brick.models.ExternalLink;

@Service
public class BrickExportCsvService {

    private final BrickExportService brickExportService;

    public BrickExportCsvService(BrickExportService brickExportService) {
        this.brickExportService = brickExportService;
    }

    public String exportStateAsCsv() {
        BrickState state = brickExportService.exportState();
        StringBuilder csv = new StringBuilder();
        csv.append("type,brickNumber,brickTitle,brickTags,brickImageBase64,linkName,linkUrl,linkEnabled\n");

        for (Brick brick : state.getBricks() != null ? state.getBricks() : java.util.List.<Brick>of()) {
            csv.append("brick").append(',');
            csv.append(CsvSupport.escapeCsv(brick != null ? brick.getNumber() : null)).append(',');
            csv.append(CsvSupport.escapeCsv(brick != null ? brick.getTitle() : null)).append(',');
            csv.append(CsvSupport.escapeCsv(joinTags(brick != null ? brick.getTags() : null), '|')).append(',');
            csv.append(CsvSupport.escapeCsv(brick != null ? brick.getImageBase64() : null)).append(',');
            csv.append(',').append(',').append('\n');
        }

        for (ExternalLink externalLink : state.getExternalLinks() != null ? state.getExternalLinks() : java.util.List.<ExternalLink>of()) {
            csv.append("external-link").append(',');
            csv.append(',').append(',').append(',').append(',');
            csv.append(CsvSupport.escapeCsv(externalLink != null ? externalLink.getName() : null)).append(',');
            csv.append(CsvSupport.escapeCsv(externalLink != null ? externalLink.getUrl() : null)).append(',');
            csv.append(externalLink != null && externalLink.isEnabled());
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
