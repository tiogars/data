package fr.tiogars.data.vehicles.car.services;

import org.springframework.stereotype.Service;

import fr.tiogars.data.common.exceptions.DataNotFoundException;
import fr.tiogars.data.vehicles.car.repositories.CarRepository;

@Service
public class CarDeleteOneService {

    private final CarRepository carRepository;

    public CarDeleteOneService(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    public void deleteCar(String id) {
        if (!carRepository.existsById(id)) {
            throw new DataNotFoundException("Voiture non trouvee pour l'id: " + id);
        }
        carRepository.deleteById(id);
    }
}
