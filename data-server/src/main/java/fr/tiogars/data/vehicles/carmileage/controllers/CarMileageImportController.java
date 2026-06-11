package fr.tiogars.data.vehicles.carmileage.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.vehicles.carmileage.forms.CarMileageImportForm;
import fr.tiogars.data.vehicles.carmileage.models.CarMileageImportResult;
import fr.tiogars.data.vehicles.carmileage.services.CarMileageImportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "car-mileage", description = "Operations liees aux releves de kilometrage des voitures.")
public class CarMileageImportController {

    private final CarMileageImportService carMileageImportService;

    public CarMileageImportController(CarMileageImportService carMileageImportService) {
        this.carMileageImportService = carMileageImportService;
    }

    @PostMapping("/car-mileage/import")
    @Operation(summary = "Importer les releves de kilometrage", description = "Importe des releves depuis un format JSON.")
    public ResponseEntity<CarMileageImportResult> importCarMileages(@RequestBody CarMileageImportForm form) {
        return ResponseEntity.ok(carMileageImportService.importCarMileages(form));
    }
}
