package fr.tiogars.data.softwares.android.forms;

import java.util.List;

import fr.tiogars.data.softwares.android.models.Android;
import io.swagger.v3.oas.annotations.media.Schema;

public class AndroidImportForm {

    @Schema(description = "Liste des applications Android a importer.")
    private List<Android> items;

    public List<Android> getItems() {
        return items;
    }

    public void setItems(List<Android> items) {
        this.items = items;
    }
}