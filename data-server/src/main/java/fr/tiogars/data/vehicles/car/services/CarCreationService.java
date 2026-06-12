package fr.tiogars.data.vehicles.car.services;

import static fr.tiogars.data.common.validation.TextValidationUtils.normalizeNullableText;
import static fr.tiogars.data.common.validation.TextValidationUtils.requireText;

import org.springframework.stereotype.Service;

import fr.tiogars.data.vehicles.car.entities.CarEntity;
import fr.tiogars.data.vehicles.car.forms.CarCreationForm;
import fr.tiogars.data.vehicles.car.models.Car;
import fr.tiogars.data.vehicles.car.repositories.CarRepository;

@Service
public class CarCreationService {

    private final CarRepository carRepository;

    public CarCreationService(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    public Car createCar(CarCreationForm form) {
        validateUniqueName(form.getName(), null);

        validateUniqueVehicleRegistrationPlate(form.getVehicleRegistrationPlate(), null);

        CarEntity entity = new CarEntity();
        applyValues(entity, form.getName(), form.getVehicleRegistrationPlate(), form.getDescription());

        return CarModelMapper.toModel(carRepository.save(entity));
    }

    static void applyValues(CarEntity entity, String name, String vehicleRegistrationPlate, String description) {
        entity.setName(requireText(name, "Le nom de la voiture est obligatoire."));
        entity.setVehicleRegistrationPlate(requireText(vehicleRegistrationPlate, "Le numéro d'immatriculation de la voiture est obligatoire."));
        entity.setDescription(normalizeNullableText(description));
    }

    void validateUniqueName(String name, String currentId) {
        carRepository.findByName(requireText(name, "Le nom de la voiture est obligatoire."))
            .filter(entity -> !entity.getId().equals(currentId))
            .ifPresent(entity -> {
                throw new IllegalArgumentException("Une voiture avec ce nom existe deja.");
            });
    }

    void validateUniqueVehicleRegistrationPlate(String vehicleRegistrationPlate, String currentId) {
        carRepository.findByVehicleRegistrationPlate(requireText(vehicleRegistrationPlate, "Le numéro d'immatriculation de la voiture est obligatoire."))
            .filter(entity -> !entity.getId().equals(currentId))
            .ifPresent(entity -> {
                throw new IllegalArgumentException("Une voiture avec ce numéro d'immatriculation existe deja.");
            });
    }
}
