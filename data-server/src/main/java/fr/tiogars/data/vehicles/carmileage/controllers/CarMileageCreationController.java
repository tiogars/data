package fr.tiogars.data.vehicles.carmileage.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.vehicles.carmileage.forms.CarMileageCreationForm;
import fr.tiogars.data.vehicles.carmileage.models.CarMileage;
import fr.tiogars.data.vehicles.carmileage.services.CarMileageCreationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "car-mileage", description = "Operations liees aux releves de kilometrage des voitures.")
public class CarMileageCreationController {

    private final CarMileageCreationService carMileageCreationService;

    public CarMileageCreationController(CarMileageCreationService carMileageCreationService) {
        this.carMileageCreationService = carMileageCreationService;
    }

    @PostMapping("/car-mileage")
    @Operation(summary = "Creer un releve", description = "Cette operation permet d'enregistrer un releve de kilometrage pour une voiture.")
    public ResponseEntity<CarMileage> createCarMileage(@RequestBody CarMileageCreationForm form) {
        return ResponseEntity.ok(carMileageCreationService.createCarMileage(form));
    }
}
