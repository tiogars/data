package fr.tiogars.data.cave.vin.forms;

import java.util.List;

import fr.tiogars.data.cave.vin.models.Vin;
import io.swagger.v3.oas.annotations.media.Schema;

public class VinImportForm {

    @Schema(description = "Liste JSON des vins a importer.")
    private List<Vin> items;

    public List<Vin> getItems() {
        return items;
    }

    public void setItems(List<Vin> items) {
        this.items = items;
    }
}
