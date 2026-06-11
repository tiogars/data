package fr.tiogars.data.vehicles.carmileage.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.vehicles.carmileage.models.CarMileageListResponse;
import fr.tiogars.data.vehicles.carmileage.services.CarMileageExportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "car-mileage", description = "Operations liees aux releves de kilometrage des voitures.")
public class CarMileageExportController {

    private final CarMileageExportService carMileageExportService;

    public CarMileageExportController(CarMileageExportService carMileageExportService) {
        this.carMileageExportService = carMileageExportService;
    }

    @GetMapping("/car-mileage/export")
    @Operation(summary = "Exporter les releves de kilometrage", description = "Retourne la liste complete des releves en JSON.")
    public ResponseEntity<CarMileageListResponse> exportCarMileages() {
        return ResponseEntity.ok(carMileageExportService.exportCarMileages());
    }
}
