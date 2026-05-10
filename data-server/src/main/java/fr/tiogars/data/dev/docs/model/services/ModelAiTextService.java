package fr.tiogars.data.dev.docs.model.services;

import java.util.List;

import org.springframework.stereotype.Service;

import fr.tiogars.data.dev.docs.model.models.Model;
import fr.tiogars.data.dev.docs.model.models.ModelAiTextResponse;
import fr.tiogars.data.dev.docs.model.models.ModelAttribute;

@Service
public class ModelAiTextService {

    private final ModelGetOneService modelGetOneService;

    public ModelAiTextService(ModelGetOneService modelGetOneService) {
        this.modelGetOneService = modelGetOneService;
    }

    public ModelAiTextResponse buildAiText(String id) {
        Model model = modelGetOneService.getModel(id);
        return new ModelAiTextResponse(model.getId(), toAiPrompt(model));
    }

    private static String toAiPrompt(Model model) {
        String modelName = model.getName() != null ? model.getName() : "Nouveau modele";
        String modelDescription = model.getDescription() != null ? model.getDescription() : "";
        List<ModelAttribute> attributes = model.getModelAttributes() != null ? model.getModelAttributes() : List.of();

        StringBuilder attributeLines = new StringBuilder();
        StringBuilder jsonAttributes = new StringBuilder();

        if (attributes.isEmpty()) {
            attributeLines.append("- Aucun attribut\n");
            jsonAttributes.append("[]");
        } else {
            jsonAttributes.append("[\n");
            for (int i = 0; i < attributes.size(); i++) {
                ModelAttribute attribute = attributes.get(i);
                String attributeName = attribute.getName() != null ? attribute.getName() : "";
                String attributeDescription = attribute.getDescription() != null ? attribute.getDescription() : "";
                attributeLines.append("- name: ").append(attributeName)
                    .append(" | description: ").append(attributeDescription)
                    .append("\n");

                jsonAttributes.append("  { \"name\": \"")
                    .append(escapeJson(attributeName))
                    .append("\", \"description\": \"")
                    .append(escapeJson(attributeDescription))
                    .append("\" }");
                if (i < attributes.size() - 1) {
                    jsonAttributes.append(",");
                }
                jsonAttributes.append("\n");
            }
            jsonAttributes.append("]");
        }

        return "Tu es un assistant de modelisation de donnees. "
            + "Cree un nouveau model inspire de la specification ci-dessous, en conservant la structure mais en adaptant si necessaire les noms et descriptions.\n\n"
            + "Modele source:\n"
            + "- name: " + modelName + "\n"
            + "- description: " + modelDescription + "\n"
            + "- attributes:\n"
            + attributeLines
            + "\nFormat de sortie attendu (JSON):\n"
            + "{\n"
            + "  \"name\": \"<nom du nouveau modele>\",\n"
            + "  \"description\": \"<description du nouveau modele>\",\n"
            + "  \"modelAttributes\": " + jsonAttributes + "\n"
            + "}";
    }

    private static String escapeJson(String value) {
        return value
            .replace("\\", "\\\\")
            .replace("\"", "\\\"")
            .replace("\n", "\\n")
            .replace("\r", "\\r")
            .replace("\t", "\\t");
    }
}
