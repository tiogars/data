package fr.tiogars.data.vehicles.car.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.vehicles.car.models.CarListResponse;
import fr.tiogars.data.vehicles.car.services.CarExportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "car", description = "Operations liees a la gestion des voitures.")
public class CarExportController {

    private final CarExportService carExportService;

    public CarExportController(CarExportService carExportService) {
        this.carExportService = carExportService;
    }

    @GetMapping("/car/export")
    @Operation(summary = "Exporter les voitures", description = "Retourne la liste complete des voitures en JSON.")
    public ResponseEntity<CarListResponse> exportCars() {
        return ResponseEntity.ok(carExportService.exportCars());
    }
}
