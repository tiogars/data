package fr.tiogars.data.cave.maison.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import fr.tiogars.data.common.csv.CsvSupport;
import fr.tiogars.data.cave.maison.forms.MaisonImportForm;
import fr.tiogars.data.cave.maison.models.Maison;
import fr.tiogars.data.cave.maison.models.MaisonImportResult;

@Service
public class MaisonImportCsvService { private final MaisonImportService maisonImportService; public MaisonImportCsvService(MaisonImportService maisonImportService) { this.maisonImportService = maisonImportService; } public MaisonImportResult importMaisonsFromCsv(String csvContent) { if (csvContent == null || csvContent.isBlank()) return maisonImportService.importMaisons(new MaisonImportForm()); char delimiter = CsvSupport.detectDelimiter(csvContent); List<List<String>> rows = CsvSupport.parseCsvRows(csvContent, delimiter); if (rows.isEmpty()) return maisonImportService.importMaisons(new MaisonImportForm()); int nameIndex = 0; int extraIndex = 1; var headers = rows.getFirst().stream().map(v -> CsvSupport.normalizeHeader(v, true)).toList(); if (headers.contains("name") || headers.contains("website")) { nameIndex = Math.max(headers.indexOf("name"), 0); extraIndex = headers.indexOf("website"); if (extraIndex < 0) extraIndex = 1; rows = rows.subList(1, rows.size()); } List<Maison> items = new ArrayList<>(); for (List<String> row : rows) { String name = CsvSupport.valueAt(row, nameIndex); String value = parseValue(CsvSupport.valueAt(row, extraIndex)); if ((name == null || name.isBlank()) && value == null) continue; Maison item = new Maison(); item.setName(name); item.setWebsite(value); items.add(item); } MaisonImportForm form = new MaisonImportForm(); form.setItems(items); return maisonImportService.importMaisons(form); } private static String parseValue(String value) { return value; } }
