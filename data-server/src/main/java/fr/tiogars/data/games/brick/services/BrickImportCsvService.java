package fr.tiogars.data.games.brick.services;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

import org.springframework.stereotype.Service;

import fr.tiogars.data.common.csv.CsvSupport;
import fr.tiogars.data.games.brick.models.Brick;
import fr.tiogars.data.games.brick.models.BrickState;
import fr.tiogars.data.games.brick.models.ExternalLink;

@Service
public class BrickImportCsvService {

    private final BrickImportService brickImportService;

    public BrickImportCsvService(BrickImportService brickImportService) {
        this.brickImportService = brickImportService;
    }

    public BrickState importStateFromCsv(String csvContent) {
        if (csvContent == null || csvContent.isBlank()) {
            return brickImportService.importState(List.of(), List.of());
        }

        char delimiter = CsvSupport.detectDelimiter(csvContent);
        List<List<String>> rows = CsvSupport.parseCsvRows(csvContent, delimiter);
        if (rows.isEmpty()) {
            return brickImportService.importState(List.of(), List.of());
        }

        CsvColumnMapping mapping = resolveColumnMapping(rows.getFirst());
        int startIndex = mapping.hasHeader() ? 1 : 0;
        List<Brick> bricks = new ArrayList<>();
        List<ExternalLink> externalLinks = new ArrayList<>();

        for (int i = startIndex; i < rows.size(); i++) {
            List<String> row = rows.get(i);
            if (row.isEmpty()) {
                continue;
            }

            String type = CsvSupport.valueAt(row, mapping.typeIndex());
            String normalizedType = type != null ? type.trim().toLowerCase() : "";

            if ("external-link".equals(normalizedType) || "externallink".equals(normalizedType) || "link".equals(normalizedType)) {
                String name = CsvSupport.valueAt(row, mapping.linkNameIndex());
                String url = CsvSupport.valueAt(row, mapping.linkUrlIndex());
                String enabled = CsvSupport.valueAt(row, mapping.linkEnabledIndex());
                if ((name == null || name.isBlank()) && (url == null || url.isBlank())) {
                    continue;
                }

                ExternalLink externalLink = new ExternalLink();
                externalLink.setName(name);
                externalLink.setUrl(url);
                externalLink.setEnabled(enabled == null || enabled.isBlank() || Boolean.parseBoolean(enabled.trim()));
                externalLinks.add(externalLink);
                continue;
            }

            String number = CsvSupport.valueAt(row, mapping.brickNumberIndex());
            String title = CsvSupport.valueAt(row, mapping.brickTitleIndex());
            String tags = CsvSupport.valueAt(row, mapping.brickTagsIndex());
            String imageBase64 = CsvSupport.valueAt(row, mapping.brickImageBase64Index());
            if ((number == null || number.isBlank()) && (title == null || title.isBlank())) {
                continue;
            }

            Brick brick = new Brick();
            brick.setNumber(number);
            brick.setTitle(title);
            brick.setTags(splitTags(tags));
            brick.setImageBase64(imageBase64);
            bricks.add(brick);
        }

        return brickImportService.importState(bricks, externalLinks);
    }

    private record CsvColumnMapping(
        boolean hasHeader,
        int typeIndex,
        int brickNumberIndex,
        int brickTitleIndex,
        int brickTagsIndex,
        int brickImageBase64Index,
        int linkNameIndex,
        int linkUrlIndex,
        int linkEnabledIndex
    ) { }

    private static CsvColumnMapping resolveColumnMapping(List<String> firstRow) {
        if (firstRow == null || firstRow.isEmpty()) {
            return new CsvColumnMapping(false, 0, 1, 2, 3, 4, 5, 6, 7);
        }

        int typeIndex = -1;
        int brickNumberIndex = -1;
        int brickTitleIndex = -1;
        int brickTagsIndex = -1;
        int brickImageBase64Index = -1;
        int linkNameIndex = -1;
        int linkUrlIndex = -1;
        int linkEnabledIndex = -1;

        for (int i = 0; i < firstRow.size(); i++) {
            String normalizedHeader = CsvSupport.normalizeHeader(firstRow.get(i), true);
            if ("type".equals(normalizedHeader)) {
                typeIndex = i;
            }
            if ("bricknumber".equals(normalizedHeader) || "number".equals(normalizedHeader)) {
                brickNumberIndex = i;
            }
            if ("bricktitle".equals(normalizedHeader) || "title".equals(normalizedHeader)) {
                brickTitleIndex = i;
            }
            if ("bricktags".equals(normalizedHeader) || "tags".equals(normalizedHeader)) {
                brickTagsIndex = i;
            }
            if ("brickimagebase64".equals(normalizedHeader) || "imagebase64".equals(normalizedHeader) || "imageurl".equals(normalizedHeader)) {
                brickImageBase64Index = i;
            }
            if ("linkname".equals(normalizedHeader) || "name".equals(normalizedHeader)) {
                linkNameIndex = i;
            }
            if ("linkurl".equals(normalizedHeader) || "url".equals(normalizedHeader)) {
                linkUrlIndex = i;
            }
            if ("linkenabled".equals(normalizedHeader) || "enabled".equals(normalizedHeader)) {
                linkEnabledIndex = i;
            }
        }

        if (typeIndex >= 0 || brickNumberIndex >= 0 || brickTitleIndex >= 0 || linkNameIndex >= 0 || linkUrlIndex >= 0) {
            return new CsvColumnMapping(
                true,
                typeIndex >= 0 ? typeIndex : 0,
                brickNumberIndex >= 0 ? brickNumberIndex : 1,
                brickTitleIndex >= 0 ? brickTitleIndex : 2,
                brickTagsIndex >= 0 ? brickTagsIndex : 3,
                brickImageBase64Index >= 0 ? brickImageBase64Index : 4,
                linkNameIndex >= 0 ? linkNameIndex : 5,
                linkUrlIndex >= 0 ? linkUrlIndex : 6,
                linkEnabledIndex >= 0 ? linkEnabledIndex : 7
            );
        }

        return new CsvColumnMapping(false, 0, 1, 2, 3, 4, 5, 6, 7);
    }

    private static List<String> splitTags(String value) {
        if (value == null || value.isBlank()) {
            return new ArrayList<>();
        }

        return new ArrayList<>(java.util.Arrays.stream(value.split("[|;,]"))
            .map(BrickImportCsvService::trim)
            .filter(item -> !item.isBlank())
            .collect(java.util.stream.Collectors.toCollection(LinkedHashSet::new)));
    }

    private static String trim(@org.jspecify.annotations.NonNull String value) {
        return value.trim();
    }
}
