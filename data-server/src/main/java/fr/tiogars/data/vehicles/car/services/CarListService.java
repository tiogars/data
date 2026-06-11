package fr.tiogars.data.vehicles.car.services;

import java.util.List;

import org.springframework.stereotype.Service;

import fr.tiogars.data.vehicles.car.entities.CarEntity;
import fr.tiogars.data.vehicles.car.models.CarListResponse;
import fr.tiogars.data.vehicles.car.repositories.CarRepository;

@Service
public class CarListService {

    private final CarRepository carRepository;

    public CarListService(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    public CarListResponse listCars() {
        List<CarEntity> entities = carRepository.findAllByOrderByNameAsc();
        return new CarListResponse(entities.stream().map(CarModelMapper::toModel).toList(), entities.size());
    }
}
