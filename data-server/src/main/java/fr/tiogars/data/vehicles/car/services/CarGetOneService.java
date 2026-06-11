package fr.tiogars.data.vehicles.car.services;

import org.springframework.stereotype.Service;

import fr.tiogars.data.common.exceptions.DataNotFoundException;
import fr.tiogars.data.vehicles.car.models.Car;
import fr.tiogars.data.vehicles.car.repositories.CarRepository;

@Service
public class CarGetOneService {

    private final CarRepository carRepository;

    public CarGetOneService(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    public Car getCar(String id) {
        return carRepository.findById(id)
            .map(CarModelMapper::toModel)
            .orElseThrow(() -> new DataNotFoundException("Voiture non trouvee pour l'id: " + id));
    }
}
