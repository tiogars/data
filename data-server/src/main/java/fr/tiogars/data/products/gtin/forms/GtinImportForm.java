package fr.tiogars.data.products.gtin.forms;

import java.util.List;

import fr.tiogars.data.products.gtin.models.Gtin;
import io.swagger.v3.oas.annotations.media.Schema;

public class GtinImportForm {

    @Schema(description = "Liste des GTIN a importer.")
    private List<Gtin> items;

    public List<Gtin> getItems() {
        return items;
    }

    public void setItems(List<Gtin> items) {
        this.items = items;
    }
}
