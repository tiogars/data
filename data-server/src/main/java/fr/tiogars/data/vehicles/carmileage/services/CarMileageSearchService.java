package fr.tiogars.data.vehicles.carmileage.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.core.TypedPropertyPath;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import fr.tiogars.data.vehicles.carmileage.entities.CarMileageEntity;
import fr.tiogars.data.vehicles.carmileage.models.CarMileage;
import fr.tiogars.data.vehicles.carmileage.models.CarMileageSearchResponse;
import fr.tiogars.data.vehicles.carmileage.repositories.CarMileageRepository;

@Service
public class CarMileageSearchService {

    private final CarMileageRepository carMileageRepository;

    public CarMileageSearchService(CarMileageRepository carMileageRepository) {
        this.carMileageRepository = carMileageRepository;
    }

    public CarMileageSearchResponse searchCarMileages(String carId, int page, int size) {
        Pageable pageable = PageRequest.of(
            page,
            size,
            Sort.by(TypedPropertyPath.of(CarMileageSearchService::getReadingAt)).descending()
        );

        Page<CarMileageEntity> result = carMileageRepository.findAll(createSearchSpecification(carId), pageable);

        List<CarMileage> items = result.getContent().stream()
            .map(CarMileageModelMapper::toModel)
            .toList();

        return new CarMileageSearchResponse(items, toSafeCount(result.getTotalElements()), page, size, carId);
    }

    private static java.time.LocalDateTime getReadingAt(@NonNull CarMileageEntity entity) {
        return entity.getReadingAt();
    }

    private Specification<CarMileageEntity> createSearchSpecification(String carId) {
        if (carId == null || carId.isBlank()) {
            return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.conjunction();
        }

        return (root, criteriaQuery, criteriaBuilder) ->
            criteriaBuilder.equal(root.get("car").get("id"), carId);
    }

    private int toSafeCount(long value) {
        return value > Integer.MAX_VALUE ? Integer.MAX_VALUE : (int) value;
    }
}
