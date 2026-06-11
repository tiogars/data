package fr.tiogars.data.vehicles.carmileage.services;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import fr.tiogars.data.common.exceptions.DataNotFoundException;
import fr.tiogars.data.vehicles.car.entities.CarEntity;
import fr.tiogars.data.vehicles.car.repositories.CarRepository;
import fr.tiogars.data.vehicles.carmileage.entities.CarMileageEntity;
import fr.tiogars.data.vehicles.carmileage.forms.CarMileageCreationForm;
import fr.tiogars.data.vehicles.carmileage.models.CarMileage;
import fr.tiogars.data.vehicles.carmileage.repositories.CarMileageRepository;

@Service
public class CarMileageCreationService {

    private final CarMileageRepository carMileageRepository;
    private final CarRepository carRepository;

    public CarMileageCreationService(CarMileageRepository carMileageRepository, CarRepository carRepository) {
        this.carMileageRepository = carMileageRepository;
        this.carRepository = carRepository;
    }

    public CarMileage createCarMileage(CarMileageCreationForm form) {
        CarEntity car = resolveCar(form.getCarId());

        CarMileageEntity entity = new CarMileageEntity();
        entity.setCar(car);
        applyValues(entity, form.getReadingAt(), form.getOdometerKm(), form.getFuelVolumeLiters(), form.getFullTank());

        return CarMileageModelMapper.toModel(carMileageRepository.save(entity));
    }

    static void applyValues(
        CarMileageEntity entity,
        LocalDateTime readingAt,
        Integer odometerKm,
        BigDecimal fuelVolumeLiters,
        Boolean fullTank
    ) {
        entity.setReadingAt(readingAt != null ? readingAt : LocalDateTime.now());

        if (odometerKm == null || odometerKm < 0) {
            throw new IllegalArgumentException("Le kilometrage est obligatoire et doit etre positif.");
        }
        entity.setOdometerKm(odometerKm);

        if (fuelVolumeLiters != null && fuelVolumeLiters.signum() < 0) {
            throw new IllegalArgumentException("Le volume de carburant ne peut pas etre negatif.");
        }
        entity.setFuelVolumeLiters(fuelVolumeLiters);

        entity.setFullTank(Boolean.TRUE.equals(fullTank));
    }

    private CarEntity resolveCar(String carId) {
        if (carId == null || carId.isBlank()) {
            throw new IllegalArgumentException("La voiture est obligatoire pour enregistrer un releve.");
        }

        return carRepository.findById(carId)
            .orElseThrow(() -> new DataNotFoundException("Voiture non trouvee pour l'id: " + carId));
    }
}
