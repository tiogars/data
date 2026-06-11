package fr.tiogars.data.settings.urlmanager.services;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

import org.springframework.stereotype.Service;

import fr.tiogars.data.common.csv.CsvSupport;
import fr.tiogars.data.settings.urlmanager.models.ManagedUrl;
import fr.tiogars.data.settings.urlmanager.models.UrlCardConfig;
import fr.tiogars.data.settings.urlmanager.models.UrlManagerState;

@Service
public class UrlManagerImportCsvService {

    private final UrlManagerImportService urlManagerImportService;

    public UrlManagerImportCsvService(UrlManagerImportService urlManagerImportService) {
        this.urlManagerImportService = urlManagerImportService;
    }

    public UrlManagerState importStateFromCsv(String csvContent) {
        if (csvContent == null || csvContent.isBlank()) {
            return urlManagerImportService.importState(new UrlManagerState(List.of(), List.of()));
        }

        char delimiter = CsvSupport.detectDelimiter(csvContent);
        List<List<String>> rows = CsvSupport.parseCsvRows(csvContent, delimiter);
        if (rows.isEmpty()) {
            return urlManagerImportService.importState(new UrlManagerState(List.of(), List.of()));
        }

        CsvColumnMapping mapping = resolveColumnMapping(rows.getFirst());
        int startIndex = mapping.hasHeader() ? 1 : 0;
        List<ManagedUrl> urls = new ArrayList<>();
        List<UrlCardConfig> cards = new ArrayList<>();

        for (int i = startIndex; i < rows.size(); i++) {
            List<String> row = rows.get(i);
            if (row.isEmpty()) {
                continue;
            }

            String type = CsvSupport.valueAt(row, mapping.typeIndex());
            String normalizedType = type != null ? type.trim().toLowerCase() : "";

            if ("card".equals(normalizedType)) {
                String title = CsvSupport.valueAt(row, mapping.titleIndex());
                String tags = CsvSupport.valueAt(row, mapping.tagsIndex());
                String matchMode = CsvSupport.valueAt(row, mapping.matchModeIndex());
                if ((title == null || title.isBlank()) && (tags == null || tags.isBlank()) && (matchMode == null || matchMode.isBlank())) {
                    continue;
                }

                UrlCardConfig card = new UrlCardConfig();
                card.setTitle(title);
                card.setTags(splitTags(tags));
                card.setMatchMode(matchMode);
                cards.add(card);
                continue;
            }

            String label = CsvSupport.valueAt(row, mapping.labelIndex());
            String url = CsvSupport.valueAt(row, mapping.urlIndex());
            String tags = CsvSupport.valueAt(row, mapping.tagsIndex());
            String description = CsvSupport.valueAt(row, mapping.descriptionIndex());
            if ((label == null || label.isBlank()) && (url == null || url.isBlank())) {
                continue;
            }

            ManagedUrl managedUrl = new ManagedUrl();
            managedUrl.setLabel(label);
            managedUrl.setUrl(url);
            managedUrl.setTags(splitTags(tags));
            managedUrl.setDescription(description);
            urls.add(managedUrl);
        }

        return urlManagerImportService.importState(new UrlManagerState(urls, cards));
    }

    private record CsvColumnMapping(boolean hasHeader, int typeIndex, int labelIndex, int urlIndex, int tagsIndex, int descriptionIndex, int titleIndex, int matchModeIndex) { }

    private static CsvColumnMapping resolveColumnMapping(List<String> firstRow) {
        if (firstRow == null || firstRow.isEmpty()) {
            return new CsvColumnMapping(false, 0, 1, 2, 3, 4, 5, 6);
        }

        int typeIndex = -1;
        int labelIndex = -1;
        int urlIndex = -1;
        int tagsIndex = -1;
        int descriptionIndex = -1;
        int titleIndex = -1;
        int matchModeIndex = -1;

        for (int i = 0; i < firstRow.size(); i++) {
            String normalizedHeader = CsvSupport.normalizeHeader(firstRow.get(i), true);
            if ("type".equals(normalizedHeader)) {
                typeIndex = i;
            }
            if ("label".equals(normalizedHeader)) {
                labelIndex = i;
            }
            if ("url".equals(normalizedHeader)) {
                urlIndex = i;
            }
            if ("tags".equals(normalizedHeader)) {
                tagsIndex = i;
            }
            if ("description".equals(normalizedHeader)) {
                descriptionIndex = i;
            }
            if ("title".equals(normalizedHeader)) {
                titleIndex = i;
            }
            if ("matchmode".equals(normalizedHeader)) {
                matchModeIndex = i;
            }
        }

        if (typeIndex >= 0 || labelIndex >= 0 || urlIndex >= 0 || titleIndex >= 0) {
            return new CsvColumnMapping(
                true,
                typeIndex >= 0 ? typeIndex : 0,
                labelIndex >= 0 ? labelIndex : 1,
                urlIndex >= 0 ? urlIndex : 2,
                tagsIndex >= 0 ? tagsIndex : 3,
                descriptionIndex >= 0 ? descriptionIndex : 4,
                titleIndex >= 0 ? titleIndex : 5,
                matchModeIndex >= 0 ? matchModeIndex : 6
            );
        }

        return new CsvColumnMapping(false, 0, 1, 2, 3, 4, 5, 6);
    }

    private static List<String> splitTags(String value) {
        if (value == null || value.isBlank()) {
            return new ArrayList<>();
        }

        return new ArrayList<>(java.util.Arrays.stream(value.split("[|;,]"))
            .map(String::trim)
            .filter(item -> !item.isBlank())
            .collect(java.util.stream.Collectors.toCollection(LinkedHashSet::new)));
    }
}
