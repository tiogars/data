package fr.tiogars.data.dev.docs.brand.forms;

import java.util.List;

import fr.tiogars.data.dev.docs.brand.models.Brand;
import io.swagger.v3.oas.annotations.media.Schema;

public class BrandImportForm {

    @Schema(description = "Liste des marques a importer.")
    private List<Brand> items;

    public List<Brand> getItems() {
        return items;
    }

    public void setItems(List<Brand> items) {
        this.items = items;
    }
}
