package fr.tiogars.data.vehicles.car.services;

import java.util.List;

import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.tiogars.data.vehicles.car.forms.CarImportForm;
import fr.tiogars.data.vehicles.car.entities.CarEntity;
import fr.tiogars.data.vehicles.car.models.Car;
import fr.tiogars.data.vehicles.car.models.CarImportResult;
import fr.tiogars.data.vehicles.car.repositories.CarRepository;

@Service
public class CarImportService {

    private final CarRepository carRepository;

    public CarImportService(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    @Transactional
    public CarImportResult importCars(CarImportForm form) {
        if (form == null || form.getItems() == null || form.getItems().isEmpty()) {
            return new CarImportResult(List.of(), 0, 0, 0, 0);
        }

        java.util.Set<String> existingNames = new java.util.HashSet<>(carRepository.findAllByOrderByNameAsc().stream()
            .map(CarImportService::getName)
            .toList());

        java.util.Set<String> existingVehicleRegistrationPlates = new java.util.HashSet<>(carRepository.findAllByOrderByNameAsc().stream()
            .map(CarImportService::getVehicleRegistrationPlate)
            .toList());

        List<Car> imported = new java.util.ArrayList<>();
        int addedCount = 0;
        int alreadyExistsCount = 0;
        int invalidCount = 0;

        for (Car item : form.getItems()) {
            try {
                if (item == null) {
                    invalidCount++;
                    continue;
                }

                String name = item.getName() != null ? item.getName().trim() : "";
                if (name.isEmpty()) {
                    invalidCount++;
                    continue;
                }

                if (existingNames.contains(name)) {
                    alreadyExistsCount++;
                    continue;
                }

                String vehicleRegistrationPlate = item.getVehicleRegistrationPlate() != null ? item.getVehicleRegistrationPlate().trim() : "";
                if (vehicleRegistrationPlate.isEmpty()) {
                    invalidCount++;
                    continue;
                }

                if (existingVehicleRegistrationPlates.contains(vehicleRegistrationPlate)) {
                    alreadyExistsCount++;
                    continue;
                }

                fr.tiogars.data.vehicles.car.entities.CarEntity entity = new fr.tiogars.data.vehicles.car.entities.CarEntity();
                CarCreationService.applyValues(entity, item.getName(), item.getVehicleRegistrationPlate(), item.getDescription());
                fr.tiogars.data.vehicles.car.entities.CarEntity saved = carRepository.save(entity);
                imported.add(CarModelMapper.toModel(saved));
                existingNames.add(saved.getName());
                existingVehicleRegistrationPlates.add(saved.getVehicleRegistrationPlate());
                addedCount++;
            } catch (RuntimeException ex) {
                invalidCount++;
            }
        }

        return new CarImportResult(imported, addedCount, alreadyExistsCount + invalidCount, alreadyExistsCount, invalidCount);
    }

    private static String getName(@NonNull CarEntity entity) {
        return entity.getName();
    }

    private static String getVehicleRegistrationPlate(@NonNull CarEntity entity) {
        return entity.getVehicleRegistrationPlate();
    }
}
