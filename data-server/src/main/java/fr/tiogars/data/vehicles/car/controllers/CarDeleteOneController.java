package fr.tiogars.data.vehicles.car.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.vehicles.car.services.CarDeleteOneService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "car", description = "Operations liees a la gestion des voitures.")
public class CarDeleteOneController {

    private final CarDeleteOneService carDeleteOneService;

    public CarDeleteOneController(CarDeleteOneService carDeleteOneService) {
        this.carDeleteOneService = carDeleteOneService;
    }

    @DeleteMapping("/car/{id}")
    @Operation(summary = "Supprimer une voiture", description = "Cette operation permet de supprimer une voiture par son identifiant.")
    public ResponseEntity<Void> deleteCar(@PathVariable String id) {
        carDeleteOneService.deleteCar(id);
        return ResponseEntity.noContent().build();
    }
}
