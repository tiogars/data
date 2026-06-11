package fr.tiogars.data.vehicles.carmileage.models;

import java.util.List;

import fr.tiogars.data.common.models.GenericListResponse;

public class CarMileageSearchResponse extends GenericListResponse<CarMileage> {

    private int page;
    private int size;
    private String carId;

    public CarMileageSearchResponse(List<CarMileage> items, int count, int page, int size, String carId) {
        super(items);
        setCount(count);
        this.page = page;
        this.size = size;
        this.carId = carId;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getCarId() {
        return carId;
    }

    public void setCarId(String carId) {
        this.carId = carId;
    }
}
