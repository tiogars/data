package fr.tiogars.data.dev.model.services;

import java.util.List;

import org.springframework.stereotype.Service;

import fr.tiogars.data.common.csv.CsvSupport;
import fr.tiogars.data.dev.model.models.Model;
import fr.tiogars.data.dev.model.models.ModelAttribute;

@Service
public class ModelExportCsvService {

    private final ModelExportService modelExportService;

    public ModelExportCsvService(ModelExportService modelExportService) {
        this.modelExportService = modelExportService;
    }

    public String exportModelsAsCsv() {
        StringBuilder csv = new StringBuilder();
        csv.append("name,description,attributes\n");

        for (Model item : modelExportService.exportModels().getItems()) {
            csv.append(CsvSupport.escapeCsv(item != null ? item.getName() : null));
            csv.append(',');
            csv.append(CsvSupport.escapeCsv(item != null ? item.getDescription() : null));
            csv.append(',');
            csv.append(CsvSupport.escapeCsv(serializeAttributes(item != null ? item.getModelAttributes() : null), '|'));
            csv.append('\n');
        }

        return csv.toString();
    }

    private static String serializeAttributes(List<ModelAttribute> attributes) {
        if (attributes == null || attributes.isEmpty()) {
            return "";
        }

        return attributes.stream()
            .filter(attribute -> attribute != null)
            .map(attribute -> {
                String name = attribute.getName() != null ? attribute.getName().trim() : "";
                String description = attribute.getDescription() != null ? attribute.getDescription().trim() : "";
                return name + "::" + description;
            })
            .filter(item -> !item.isBlank())
            .collect(java.util.stream.Collectors.joining("|"));
    }
}
