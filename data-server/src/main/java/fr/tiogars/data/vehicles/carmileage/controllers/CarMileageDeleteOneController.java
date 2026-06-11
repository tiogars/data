package fr.tiogars.data.vehicles.carmileage.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.vehicles.carmileage.services.CarMileageDeleteOneService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "car-mileage", description = "Operations liees aux releves de kilometrage des voitures.")
public class CarMileageDeleteOneController {

    private final CarMileageDeleteOneService carMileageDeleteOneService;

    public CarMileageDeleteOneController(CarMileageDeleteOneService carMileageDeleteOneService) {
        this.carMileageDeleteOneService = carMileageDeleteOneService;
    }

    @DeleteMapping("/car-mileage/{id}")
    @Operation(summary = "Supprimer un releve", description = "Cette operation permet de supprimer un releve par son identifiant.")
    public ResponseEntity<Void> deleteCarMileage(@PathVariable String id) {
        carMileageDeleteOneService.deleteCarMileage(id);
        return ResponseEntity.noContent().build();
    }
}
