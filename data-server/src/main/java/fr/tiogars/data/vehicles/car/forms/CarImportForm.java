package fr.tiogars.data.vehicles.car.forms;

import java.util.List;

import fr.tiogars.data.vehicles.car.models.Car;
import io.swagger.v3.oas.annotations.media.Schema;

public class CarImportForm {

    @Schema(description = "Liste des voitures a importer.")
    private List<Car> items;

    public List<Car> getItems() {
        return items;
    }

    public void setItems(List<Car> items) {
        this.items = items;
    }
}
