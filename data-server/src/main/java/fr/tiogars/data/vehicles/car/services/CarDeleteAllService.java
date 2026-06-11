package fr.tiogars.data.vehicles.car.services;

import org.springframework.stereotype.Service;

import fr.tiogars.data.vehicles.car.repositories.CarRepository;

@Service
public class CarDeleteAllService {

    private final CarRepository carRepository;

    public CarDeleteAllService(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    public void deleteAllCars() {
        carRepository.deleteAllInBatch();
    }
}
