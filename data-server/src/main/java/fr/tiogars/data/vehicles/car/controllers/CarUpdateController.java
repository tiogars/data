package fr.tiogars.data.vehicles.car.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.vehicles.car.models.Car;
import fr.tiogars.data.vehicles.car.services.CarUpdateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "car", description = "Operations liees a la gestion des voitures.")
public class CarUpdateController {

    private final CarUpdateService carUpdateService;

    public CarUpdateController(CarUpdateService carUpdateService) {
        this.carUpdateService = carUpdateService;
    }

    @PutMapping("/car/{id}")
    @Operation(summary = "Mettre a jour une voiture", description = "Cette operation permet de modifier une voiture existante.")
    public ResponseEntity<Car> updateCar(@PathVariable String id, @RequestBody Car carUpdate) {
        return ResponseEntity.ok(carUpdateService.updateCar(id, carUpdate));
    }
}
