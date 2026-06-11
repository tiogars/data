package fr.tiogars.data.vehicles.car.models;

import java.util.List;

import fr.tiogars.data.common.models.GenericListResponse;

public class CarListResponse extends GenericListResponse<Car> {

    public CarListResponse(List<Car> items, int count) {
        super(items);
        setCount(count);
    }
}
