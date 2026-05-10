package fr.tiogars.data.dev.docs.model.forms;

import java.util.List;

import fr.tiogars.data.dev.docs.model.models.Model;
import io.swagger.v3.oas.annotations.media.Schema;

public class ModelImportForm {

    @Schema(description = "Liste des modeles a importer.")
    private List<Model> items;

    public List<Model> getItems() {
        return items;
    }

    public void setItems(List<Model> items) {
        this.items = items;
    }
}
