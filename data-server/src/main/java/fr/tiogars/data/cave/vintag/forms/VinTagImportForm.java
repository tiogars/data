package fr.tiogars.data.cave.vintag.forms;

import java.util.List;

import fr.tiogars.data.cave.vintag.models.VinTag;
import io.swagger.v3.oas.annotations.media.Schema;

public class VinTagImportForm {
    @Schema(description = "Texte a importer. Chaque ligne non vide represente le nom d'une valeur.", example = "Millesime\nExemple")
    private String text;
    @Schema(description = "Format historique JSON: liste des tags de vin a importer.")
    private List<VinTag> items;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }


    public List<VinTag> getItems() {
        return items;
    }

    public void setItems(List<VinTag> items) {
        this.items = items;
    }

}
