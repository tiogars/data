package fr.tiogars.data.vehicles.carmileage.services;

import org.springframework.stereotype.Service;

import fr.tiogars.data.common.exceptions.DataNotFoundException;
import fr.tiogars.data.vehicles.carmileage.models.CarMileage;
import fr.tiogars.data.vehicles.carmileage.repositories.CarMileageRepository;

@Service
public class CarMileageGetOneService {

    private final CarMileageRepository carMileageRepository;

    public CarMileageGetOneService(CarMileageRepository carMileageRepository) {
        this.carMileageRepository = carMileageRepository;
    }

    public CarMileage getCarMileage(String id) {
        return carMileageRepository.findById(id)
            .map(CarMileageModelMapper::toModel)
            .orElseThrow(() -> new DataNotFoundException("Releve non trouve pour l'id: " + id));
    }
}
