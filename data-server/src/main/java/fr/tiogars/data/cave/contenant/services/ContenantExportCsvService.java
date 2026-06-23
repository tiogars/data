package fr.tiogars.data.cave.contenant.services;

import org.springframework.stereotype.Service;
import fr.tiogars.data.common.csv.CsvSupport;
import fr.tiogars.data.cave.contenant.models.Contenant;

@Service
public class ContenantExportCsvService { private final ContenantExportService contenantExportService; public ContenantExportCsvService(ContenantExportService contenantExportService) { this.contenantExportService = contenantExportService; } public String exportContenantsAsCsv() { StringBuilder csv = new StringBuilder(); csv.append("name,volume_cl\n"); for (Contenant item : contenantExportService.exportContenants().getItems()) { csv.append(CsvSupport.escapeCsv(item != null ? item.getName() : null)).append(',').append(CsvSupport.escapeCsv(item != null && item.getVolumeCl() != null ? String.valueOf(item.getVolumeCl()) : null)).append('\n'); } return csv.toString(); } }
