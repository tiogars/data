package fr.tiogars.data.cave.maison.services;

import org.springframework.stereotype.Service;
import fr.tiogars.data.common.csv.CsvSupport;
import fr.tiogars.data.cave.maison.models.Maison;

@Service
public class MaisonExportCsvService { private final MaisonExportService maisonExportService; public MaisonExportCsvService(MaisonExportService maisonExportService) { this.maisonExportService = maisonExportService; } public String exportMaisonsAsCsv() { StringBuilder csv = new StringBuilder(); csv.append("name,website\n"); for (Maison item : maisonExportService.exportMaisons().getItems()) { csv.append(CsvSupport.escapeCsv(item != null ? item.getName() : null)).append(',').append(CsvSupport.escapeCsv(item != null && item.getWebsite() != null ? String.valueOf(item.getWebsite()) : null)).append('\n'); } return csv.toString(); } }
