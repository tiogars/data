package fr.tiogars.data.cave.cepage.forms;

import java.util.List;

import fr.tiogars.data.cave.cepage.models.Cepage;
import io.swagger.v3.oas.annotations.media.Schema;

public class CepageImportForm {
    @Schema(description = "Texte a importer. Chaque ligne non vide represente le nom d'une valeur.", example = "Chardonnay\nExemple")
    private String text;
    @Schema(description = "Format historique JSON: liste des cépages a importer.")
    private List<Cepage> items;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }


    public List<Cepage> getItems() {
        return items;
    }

    public void setItems(List<Cepage> items) {
        this.items = items;
    }

}
