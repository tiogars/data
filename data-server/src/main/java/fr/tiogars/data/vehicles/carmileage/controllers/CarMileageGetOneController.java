package fr.tiogars.data.vehicles.carmileage.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.vehicles.carmileage.models.CarMileage;
import fr.tiogars.data.vehicles.carmileage.services.CarMileageGetOneService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "car-mileage", description = "Operations liees aux releves de kilometrage des voitures.")
public class CarMileageGetOneController {

    private final CarMileageGetOneService carMileageGetOneService;

    public CarMileageGetOneController(CarMileageGetOneService carMileageGetOneService) {
        this.carMileageGetOneService = carMileageGetOneService;
    }

    @GetMapping("/car-mileage/{id}")
    @Operation(summary = "Recuperer un releve", description = "Cette operation permet de recuperer un releve par son identifiant.")
    public ResponseEntity<CarMileage> getCarMileage(@PathVariable String id) {
        return ResponseEntity.ok(carMileageGetOneService.getCarMileage(id));
    }
}
