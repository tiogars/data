package fr.tiogars.data.vehicles.carmileage.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.vehicles.carmileage.models.CarMileage;
import fr.tiogars.data.vehicles.carmileage.services.CarMileageUpdateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "car-mileage", description = "Operations liees aux releves de kilometrage des voitures.")
public class CarMileageUpdateController {

    private final CarMileageUpdateService carMileageUpdateService;

    public CarMileageUpdateController(CarMileageUpdateService carMileageUpdateService) {
        this.carMileageUpdateService = carMileageUpdateService;
    }

    @PutMapping("/car-mileage/{id}")
    @Operation(summary = "Mettre a jour un releve", description = "Cette operation permet de modifier un releve existant.")
    public ResponseEntity<CarMileage> updateCarMileage(@PathVariable String id, @RequestBody CarMileage update) {
        return ResponseEntity.ok(carMileageUpdateService.updateCarMileage(id, update));
    }
}
