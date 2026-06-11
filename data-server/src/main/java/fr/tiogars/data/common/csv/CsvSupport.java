package fr.tiogars.data.common.csv;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public final class CsvSupport {

    private CsvSupport() {
    }

    public static char detectDelimiter(String content) {
        if (content == null || content.isBlank()) {
            return ',';
        }

        String[] lines = content.split("\\R");
        for (String line : lines) {
            if (line == null || line.isBlank()) {
                continue;
            }
            int semicolonCount = 0;
            int commaCount = 0;
            for (int i = 0; i < line.length(); i++) {
                char ch = line.charAt(i);
                if (ch == ';') {
                    semicolonCount++;
                }
                if (ch == ',') {
                    commaCount++;
                }
            }
            return semicolonCount > commaCount ? ';' : ',';
        }

        return ',';
    }

    public static String escapeCsv(String value) {
        if (value == null) {
            return "";
        }

        boolean requiresQuotes = value.indexOf(',') >= 0
            || value.indexOf('"') >= 0
            || value.indexOf('\n') >= 0
            || value.indexOf('\r') >= 0;

        String escaped = value.replace("\"", "\"\"");
        return requiresQuotes ? "\"" + escaped + "\"" : escaped;
    }

    public static List<List<String>> parseCsvRows(String content, char delimiter) {
        List<List<String>> rows = new ArrayList<>();
        List<String> currentRow = new ArrayList<>();
        StringBuilder currentValue = new StringBuilder();
        boolean inQuotes = false;

        for (int i = 0; i < content.length(); i++) {
            char ch = content.charAt(i);

            if (ch == '"') {
                if (inQuotes && i + 1 < content.length() && content.charAt(i + 1) == '"') {
                    currentValue.append('"');
                    i++;
                } else {
                    inQuotes = !inQuotes;
                }
                continue;
            }

            if (!inQuotes && ch == delimiter) {
                currentRow.add(currentValue.toString());
                currentValue.setLength(0);
                continue;
            }

            if (!inQuotes && (ch == '\n' || ch == '\r')) {
                if (ch == '\r' && i + 1 < content.length() && content.charAt(i + 1) == '\n') {
                    i++;
                }
                currentRow.add(currentValue.toString());
                currentValue.setLength(0);
                if (!(currentRow.size() == 1 && currentRow.get(0).isBlank())) {
                    rows.add(currentRow);
                }
                currentRow = new ArrayList<>();
                continue;
            }

            currentValue.append(ch);
        }

        currentRow.add(currentValue.toString());
        if (!(currentRow.size() == 1 && currentRow.get(0).isBlank())) {
            rows.add(currentRow);
        }

        return rows;
    }

    public static String valueAt(List<String> row, int index) {
        if (row == null || index < 0 || index >= row.size()) {
            return null;
        }
        return row.get(index);
    }

    public static String normalizeHeader(String value) {
        if (value == null) {
            return "";
        }
        return value
            .replace("\uFEFF", "")
            .trim()
            .toLowerCase(Locale.ROOT);
    }
}
