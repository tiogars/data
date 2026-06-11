package fr.tiogars.data.vehicles.carmileage.services;

import java.util.List;

import org.springframework.stereotype.Service;

import fr.tiogars.data.common.exceptions.DataNotFoundException;
import fr.tiogars.data.vehicles.car.entities.CarEntity;
import fr.tiogars.data.vehicles.car.repositories.CarRepository;
import fr.tiogars.data.vehicles.carmileage.models.CarMileageChartPoint;
import fr.tiogars.data.vehicles.carmileage.models.CarMileageChartResponse;
import fr.tiogars.data.vehicles.carmileage.repositories.CarMileageRepository;

@Service
public class CarMileageChartService {

    private final CarMileageRepository carMileageRepository;
    private final CarRepository carRepository;

    public CarMileageChartService(CarMileageRepository carMileageRepository, CarRepository carRepository) {
        this.carMileageRepository = carMileageRepository;
        this.carRepository = carRepository;
    }

    public CarMileageChartResponse chartCarMileages(String carId) {
        if (carId == null || carId.isBlank()) {
            throw new IllegalArgumentException("Le parametre carId est obligatoire pour le graphique.");
        }

        CarEntity car = carRepository.findById(carId)
            .orElseThrow(() -> new DataNotFoundException("Voiture non trouvee pour l'id: " + carId));

        List<CarMileageChartPoint> points = carMileageRepository.findByCar_IdOrderByReadingAtAsc(carId).stream()
            .map(entity -> new CarMileageChartPoint(entity.getReadingAt(), entity.getOdometerKm()))
            .toList();

        return new CarMileageChartResponse(car.getId(), car.getName(), points);
    }
}
