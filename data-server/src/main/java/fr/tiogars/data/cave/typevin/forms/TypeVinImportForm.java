package fr.tiogars.data.cave.typevin.forms;

import java.util.List;

import fr.tiogars.data.cave.typevin.models.TypeVin;
import io.swagger.v3.oas.annotations.media.Schema;

public class TypeVinImportForm {
    @Schema(description = "Texte a importer. Chaque ligne non vide represente le nom d'une valeur.", example = "Brut\nExemple")
    private String text;
    @Schema(description = "Format historique JSON: liste des types de vin a importer.")
    private List<TypeVin> items;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }


    public List<TypeVin> getItems() {
        return items;
    }

    public void setItems(List<TypeVin> items) {
        this.items = items;
    }

}
