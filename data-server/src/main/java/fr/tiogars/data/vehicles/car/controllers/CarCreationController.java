package fr.tiogars.data.vehicles.car.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.vehicles.car.forms.CarCreationForm;
import fr.tiogars.data.vehicles.car.models.Car;
import fr.tiogars.data.vehicles.car.services.CarCreationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "car", description = "Operations liees a la gestion des voitures.")
public class CarCreationController {

    private final CarCreationService carCreationService;

    public CarCreationController(CarCreationService carCreationService) {
        this.carCreationService = carCreationService;
    }

    @PostMapping("/car")
    @Operation(summary = "Creer une voiture", description = "Cette operation permet de creer une voiture.")
    public ResponseEntity<Car> createCar(@RequestBody CarCreationForm form) {
        return ResponseEntity.ok(carCreationService.createCar(form));
    }
}
