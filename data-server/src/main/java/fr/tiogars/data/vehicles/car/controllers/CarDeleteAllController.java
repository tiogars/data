package fr.tiogars.data.vehicles.car.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.vehicles.car.services.CarDeleteAllService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "car", description = "Operations liees a la gestion des voitures.")
public class CarDeleteAllController {

    private final CarDeleteAllService carDeleteAllService;

    public CarDeleteAllController(CarDeleteAllService carDeleteAllService) {
        this.carDeleteAllService = carDeleteAllService;
    }

    @DeleteMapping("/car")
    @Operation(summary = "Supprimer toutes les voitures", description = "Cette operation permet de supprimer toutes les voitures.")
    public ResponseEntity<Void> deleteAllCars() {
        carDeleteAllService.deleteAllCars();
        return ResponseEntity.noContent().build();
    }
}
