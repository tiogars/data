package fr.tiogars.data.cave.couleur.forms;

import java.util.List;

import fr.tiogars.data.cave.couleur.models.Couleur;
import io.swagger.v3.oas.annotations.media.Schema;

public class CouleurImportForm {
    @Schema(description = "Texte a importer. Chaque ligne non vide represente le nom d'une valeur.", example = "Rouge\nExemple")
    private String text;
    @Schema(description = "Format historique JSON: liste des couleurs a importer.")
    private List<Couleur> items;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }


    public List<Couleur> getItems() {
        return items;
    }

    public void setItems(List<Couleur> items) {
        this.items = items;
    }

}
