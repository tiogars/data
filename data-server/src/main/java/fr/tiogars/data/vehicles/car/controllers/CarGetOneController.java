package fr.tiogars.data.vehicles.car.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.vehicles.car.models.Car;
import fr.tiogars.data.vehicles.car.services.CarGetOneService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "car", description = "Operations liees a la gestion des voitures.")
public class CarGetOneController {

    private final CarGetOneService carGetOneService;

    public CarGetOneController(CarGetOneService carGetOneService) {
        this.carGetOneService = carGetOneService;
    }

    @GetMapping("/car/{id}")
    @Operation(summary = "Recuperer une voiture", description = "Cette operation permet de recuperer une voiture par son identifiant.")
    public ResponseEntity<Car> getCar(@PathVariable String id) {
        return ResponseEntity.ok(carGetOneService.getCar(id));
    }
}
