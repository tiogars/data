package fr.tiogars.data.vehicles.carmileage.services;

import org.springframework.stereotype.Service;

import fr.tiogars.data.common.exceptions.DataNotFoundException;
import fr.tiogars.data.vehicles.car.entities.CarEntity;
import fr.tiogars.data.vehicles.car.repositories.CarRepository;
import fr.tiogars.data.vehicles.carmileage.entities.CarMileageEntity;
import fr.tiogars.data.vehicles.carmileage.models.CarMileage;
import fr.tiogars.data.vehicles.carmileage.repositories.CarMileageRepository;

@Service
public class CarMileageUpdateService {

    private final CarMileageRepository carMileageRepository;
    private final CarRepository carRepository;

    public CarMileageUpdateService(CarMileageRepository carMileageRepository, CarRepository carRepository) {
        this.carMileageRepository = carMileageRepository;
        this.carRepository = carRepository;
    }

    public CarMileage updateCarMileage(String id, CarMileage update) {
        CarMileageEntity entity = carMileageRepository.findById(id)
            .orElseThrow(() -> new DataNotFoundException("Releve non trouve pour l'id: " + id));

        CarEntity car = resolveCar(update.getCarId());
        entity.setCar(car);

        CarMileageCreationService.applyValues(
            entity,
            update.getReadingAt(),
            update.getOdometerKm(),
            update.getFuelVolumeLiters(),
            update.getFullTank()
        );

        return CarMileageModelMapper.toModel(carMileageRepository.save(entity));
    }

    private CarEntity resolveCar(String carId) {
        if (carId == null || carId.isBlank()) {
            throw new IllegalArgumentException("La voiture est obligatoire pour enregistrer un releve.");
        }

        return carRepository.findById(carId)
            .orElseThrow(() -> new DataNotFoundException("Voiture non trouvee pour l'id: " + carId));
    }
}
