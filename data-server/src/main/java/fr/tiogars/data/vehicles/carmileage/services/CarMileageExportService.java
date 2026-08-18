package fr.tiogars.data.vehicles.carmileage.services;

import java.util.List;

import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;

import fr.tiogars.data.vehicles.carmileage.entities.CarMileageEntity;
import fr.tiogars.data.vehicles.carmileage.models.CarMileage;
import fr.tiogars.data.vehicles.carmileage.models.CarMileageListResponse;
import fr.tiogars.data.vehicles.carmileage.repositories.CarMileageRepository;

@Service
public class CarMileageExportService {

    private final CarMileageRepository carMileageRepository;

    public CarMileageExportService(CarMileageRepository carMileageRepository) {
        this.carMileageRepository = carMileageRepository;
    }

    public CarMileageListResponse exportCarMileages() {
        List<CarMileage> items = carMileageRepository.findAll().stream()
            .sorted(java.util.Comparator.comparing(CarMileageExportService::getReadingAt, java.util.Comparator.nullsLast(java.util.Comparator.naturalOrder())))
            .map(CarMileageModelMapper::toModel)
            .toList();

        return new CarMileageListResponse(items, items.size());
    }

    private static java.time.LocalDateTime getReadingAt(@NonNull CarMileageEntity entity) {
        return entity.getReadingAt();
    }
}
