package fr.tiogars.data.vehicles.carmileage.services;

import fr.tiogars.data.vehicles.carmileage.entities.CarMileageEntity;
import fr.tiogars.data.vehicles.carmileage.models.CarMileage;

final class CarMileageModelMapper {

    private CarMileageModelMapper() {
    }

    static CarMileage toModel(CarMileageEntity entity) {
        CarMileage model = new CarMileage();
        model.setId(entity.getId());
        model.setCarId(entity.getCar().getId());
        model.setCarName(entity.getCar().getName());
        model.setReadingAt(entity.getReadingAt());
        model.setOdometerKm(entity.getOdometerKm());
        model.setFuelVolumeLiters(entity.getFuelVolumeLiters());
        model.setFullTank(entity.isFullTank());
        return model;
    }
}
