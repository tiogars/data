package fr.tiogars.data.vehicles.car.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import fr.tiogars.data.vehicles.car.entities.CarEntity;
import fr.tiogars.data.vehicles.car.models.Car;
import fr.tiogars.data.vehicles.car.models.CarSearchResponse;
import fr.tiogars.data.vehicles.car.repositories.CarRepository;

@Service
public class CarSearchService {

    private final CarRepository carRepository;

    public CarSearchService(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    public CarSearchResponse searchCars(int page, int size, String query) {
        String normalizedQuery = normalizeQuery(query);

        Pageable pageable = PageRequest.of(
            page,
            size,
            Sort.by(Sort.Order.asc("name"))
        );

        Page<CarEntity> result = carRepository.findAll(createSearchSpecification(normalizedQuery), pageable);

        List<Car> items = result.getContent().stream()
            .map(CarModelMapper::toModel)
            .toList();

        return new CarSearchResponse(items, toSafeCount(result.getTotalElements()), page, size, normalizedQuery);
    }

    private Specification<CarEntity> createSearchSpecification(String query) {
        if (query == null) {
            return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.conjunction();
        }

        String likePattern = "%" + query.toLowerCase() + "%";

        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.or(
            criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), likePattern),
            criteriaBuilder.like(criteriaBuilder.lower(criteriaBuilder.coalesce(root.get("description"), "")), likePattern)
        );
    }

    private String normalizeQuery(String query) {
        if (query == null || query.isBlank()) {
            return null;
        }
        return query.trim();
    }

    private int toSafeCount(long value) {
        return value > Integer.MAX_VALUE ? Integer.MAX_VALUE : (int) value;
    }
}
