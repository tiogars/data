package fr.tiogars.data.vehicles.car.services;

import fr.tiogars.data.vehicles.car.entities.CarEntity;
import fr.tiogars.data.vehicles.car.models.Car;

final class CarModelMapper {

    private CarModelMapper() {
    }

    static Car toModel(CarEntity entity) {
        Car model = new Car();
        model.setId(entity.getId());
        model.setName(entity.getName());
        model.setDescription(entity.getDescription());
        return model;
    }
}
