package fr.tiogars.data.products.brand.forms;

import java.util.List;

import fr.tiogars.data.products.brand.models.Brand;
import io.swagger.v3.oas.annotations.media.Schema;

public class BrandImportForm {

    @Schema(
        description = "Texte a importer. Chaque ligne non vide represente un nom de marque.",
        example = "Lego\nMattel\nHasbro"
    )
    private String text;

    @Schema(description = "Format historique JSON: liste des marques a importer.")
    private List<Brand> items;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<Brand> getItems() {
        return items;
    }

    public void setItems(List<Brand> items) {
        this.items = items;
    }
}
