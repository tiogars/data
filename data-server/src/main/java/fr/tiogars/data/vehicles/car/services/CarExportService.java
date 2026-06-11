package fr.tiogars.data.vehicles.car.services;

import java.util.List;

import org.springframework.stereotype.Service;

import fr.tiogars.data.vehicles.car.models.Car;
import fr.tiogars.data.vehicles.car.models.CarListResponse;
import fr.tiogars.data.vehicles.car.repositories.CarRepository;

@Service
public class CarExportService {

    private final CarRepository carRepository;

    public CarExportService(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    public CarListResponse exportCars() {
        List<Car> items = carRepository.findAllByOrderByNameAsc().stream()
            .map(CarModelMapper::toModel)
            .toList();

        return new CarListResponse(items, items.size());
    }
}
