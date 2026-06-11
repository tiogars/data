package fr.tiogars.data.vehicles.carmileage.models;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Reponse contenant une liste de releves de kilometrage.")
public class CarMileageListResponse {

    @Schema(description = "Liste des releves de kilometrage.")
    private List<CarMileage> items;

    @Schema(description = "Nombre total de releves.", example = "120")
    private long count;

    public CarMileageListResponse() {
    }

    public CarMileageListResponse(List<CarMileage> items, long count) {
        this.items = items;
        this.count = count;
    }

    public List<CarMileage> getItems() {
        return items;
    }

    public void setItems(List<CarMileage> items) {
        this.items = items;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }
}
