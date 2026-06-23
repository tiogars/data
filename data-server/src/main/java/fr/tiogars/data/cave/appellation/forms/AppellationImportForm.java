package fr.tiogars.data.cave.appellation.forms;

import java.util.List;

import fr.tiogars.data.cave.appellation.models.Appellation;
import io.swagger.v3.oas.annotations.media.Schema;

public class AppellationImportForm {
    @Schema(description = "Texte a importer. Chaque ligne non vide represente le nom d'une valeur.", example = "Champagne\nExemple")
    private String text;
    @Schema(description = "Format historique JSON: liste des appellations a importer.")
    private List<Appellation> items;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }


    public List<Appellation> getItems() {
        return items;
    }

    public void setItems(List<Appellation> items) {
        this.items = items;
    }

}
