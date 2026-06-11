package fr.tiogars.data.vehicles.carmileage.forms;

import java.util.List;

import fr.tiogars.data.vehicles.carmileage.models.CarMileage;
import io.swagger.v3.oas.annotations.media.Schema;

public class CarMileageImportForm {

    @Schema(description = "Liste des releves de kilometrage a importer.")
    private List<CarMileage> items;

    public List<CarMileage> getItems() {
        return items;
    }

    public void setItems(List<CarMileage> items) {
        this.items = items;
    }
}
