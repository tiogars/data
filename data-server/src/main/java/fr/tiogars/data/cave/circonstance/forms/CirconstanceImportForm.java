package fr.tiogars.data.cave.circonstance.forms;

import java.util.List;

import fr.tiogars.data.cave.circonstance.models.Circonstance;
import io.swagger.v3.oas.annotations.media.Schema;

public class CirconstanceImportForm {
    @Schema(description = "Texte a importer. Chaque ligne non vide represente le nom d'une valeur.", example = "Anniversaire\nExemple")
    private String text;
    @Schema(description = "Format historique JSON: liste des circonstances a importer.")
    private List<Circonstance> items;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }


    public List<Circonstance> getItems() {
        return items;
    }

    public void setItems(List<Circonstance> items) {
        this.items = items;
    }

}
