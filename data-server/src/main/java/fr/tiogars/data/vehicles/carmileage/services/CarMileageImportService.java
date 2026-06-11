package fr.tiogars.data.vehicles.carmileage.services;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.tiogars.data.vehicles.carmileage.entities.CarMileageEntity;
import fr.tiogars.data.vehicles.carmileage.forms.CarMileageImportForm;
import fr.tiogars.data.vehicles.carmileage.models.CarMileage;
import fr.tiogars.data.vehicles.carmileage.models.CarMileageImportResult;
import fr.tiogars.data.vehicles.carmileage.repositories.CarMileageRepository;

@Service
public class CarMileageImportService {

    private final CarMileageRepository carMileageRepository;
    private final fr.tiogars.data.vehicles.car.repositories.CarRepository carRepository;

    public CarMileageImportService(
        CarMileageRepository carMileageRepository,
        fr.tiogars.data.vehicles.car.repositories.CarRepository carRepository
    ) {
        this.carMileageRepository = carMileageRepository;
        this.carRepository = carRepository;
    }

    @Transactional
    public CarMileageImportResult importCarMileages(CarMileageImportForm form) {
        if (form == null || form.getItems() == null || form.getItems().isEmpty()) {
            return new CarMileageImportResult(List.of(), 0, 0, 0, 0);
        }

        List<CarMileageEntity> existing = carMileageRepository.findAll();
        java.util.Set<String> existingKeys = new java.util.HashSet<>();
        for (CarMileageEntity entity : existing) {
            existingKeys.add(buildKey(
                entity.getCar() != null ? entity.getCar().getId() : null,
                entity.getReadingAt(),
                entity.getOdometerKm()
            ));
        }

        List<CarMileage> imported = new java.util.ArrayList<>();
        int addedCount = 0;
        int alreadyExistsCount = 0;
        int invalidCount = 0;

        for (CarMileage item : form.getItems()) {
            try {
                if (item == null) {
                    invalidCount++;
                    continue;
                }

                String carId = item.getCarId() != null ? item.getCarId().trim() : "";
                if (carId.isEmpty()) {
                    invalidCount++;
                    continue;
                }

                fr.tiogars.data.vehicles.car.entities.CarEntity car = carRepository.findById(carId).orElse(null);
                if (car == null) {
                    invalidCount++;
                    continue;
                }

                LocalDateTime readingAt = item.getReadingAt() != null ? item.getReadingAt() : LocalDateTime.now();
                Integer odometerKm = item.getOdometerKm();
                String key = buildKey(carId, readingAt, odometerKm);

                if (existingKeys.contains(key)) {
                    alreadyExistsCount++;
                    continue;
                }

                CarMileageEntity entity = new CarMileageEntity();
                entity.setCar(car);
                CarMileageCreationService.applyValues(
                    entity,
                    item.getReadingAt(),
                    item.getOdometerKm(),
                    item.getFuelVolumeLiters(),
                    item.getFullTank()
                );
                CarMileageEntity saved = carMileageRepository.save(entity);
                imported.add(CarMileageModelMapper.toModel(saved));
                existingKeys.add(buildKey(carId, saved.getReadingAt(), saved.getOdometerKm()));
                addedCount++;
            } catch (RuntimeException ex) {
                invalidCount++;
            }
        }

        return new CarMileageImportResult(imported, addedCount, alreadyExistsCount + invalidCount, alreadyExistsCount, invalidCount);
    }

    private static String buildKey(String carId, LocalDateTime readingAt, Integer odometerKm) {
        return (carId != null ? carId : "") + "|" + (readingAt != null ? readingAt.toString() : "") + "|" + (odometerKm != null ? odometerKm : "");
    }
}
