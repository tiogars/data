package fr.tiogars.data.cave.contenant.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import fr.tiogars.data.common.csv.CsvSupport;
import fr.tiogars.data.cave.contenant.forms.ContenantImportForm;
import fr.tiogars.data.cave.contenant.models.Contenant;
import fr.tiogars.data.cave.contenant.models.ContenantImportResult;

@Service
public class ContenantImportCsvService { private final ContenantImportService contenantImportService; public ContenantImportCsvService(ContenantImportService contenantImportService) { this.contenantImportService = contenantImportService; } public ContenantImportResult importContenantsFromCsv(String csvContent) { if (csvContent == null || csvContent.isBlank()) return contenantImportService.importContenants(new ContenantImportForm()); char delimiter = CsvSupport.detectDelimiter(csvContent); List<List<String>> rows = CsvSupport.parseCsvRows(csvContent, delimiter); if (rows.isEmpty()) return contenantImportService.importContenants(new ContenantImportForm()); int nameIndex = 0; int extraIndex = 1; var headers = rows.getFirst().stream().map(v -> CsvSupport.normalizeHeader(v, true)).toList(); if (headers.contains("name") || headers.contains("volumecl")) { nameIndex = Math.max(headers.indexOf("name"), 0); extraIndex = headers.indexOf("volumecl"); if (extraIndex < 0) extraIndex = 1; rows = rows.subList(1, rows.size()); } List<Contenant> items = new ArrayList<>(); for (List<String> row : rows) { String name = CsvSupport.valueAt(row, nameIndex); Integer value = parseValue(CsvSupport.valueAt(row, extraIndex)); if ((name == null || name.isBlank()) && value == null) continue; Contenant item = new Contenant(); item.setName(name); item.setVolumeCl(value); items.add(item); } ContenantImportForm form = new ContenantImportForm(); form.setItems(items); return contenantImportService.importContenants(form); } private static Integer parseValue(String value) { if (value == null || value.isBlank()) return null; try { return Integer.parseInt(value.trim()); } catch (RuntimeException ex) { return null; } } }
