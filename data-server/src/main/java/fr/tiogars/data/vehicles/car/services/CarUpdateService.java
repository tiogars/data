package fr.tiogars.data.vehicles.car.services;

import org.springframework.stereotype.Service;

import fr.tiogars.data.common.exceptions.DataNotFoundException;
import fr.tiogars.data.vehicles.car.entities.CarEntity;
import fr.tiogars.data.vehicles.car.models.Car;
import fr.tiogars.data.vehicles.car.repositories.CarRepository;

@Service
public class CarUpdateService {

    private final CarRepository carRepository;
    private final CarCreationService carCreationService;

    public CarUpdateService(CarRepository carRepository, CarCreationService carCreationService) {
        this.carRepository = carRepository;
        this.carCreationService = carCreationService;
    }

    public Car updateCar(String id, Car carUpdate) {
        CarEntity entity = carRepository.findById(id)
            .orElseThrow(() -> new DataNotFoundException("Voiture non trouvee pour l'id: " + id));

        carCreationService.validateUniqueName(carUpdate.getName(), id);
        CarCreationService.applyValues(entity, carUpdate.getName(), carUpdate.getDescription());

        return CarModelMapper.toModel(carRepository.save(entity));
    }
}
