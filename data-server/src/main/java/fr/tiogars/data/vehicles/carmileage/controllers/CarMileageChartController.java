package fr.tiogars.data.vehicles.carmileage.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.vehicles.carmileage.models.CarMileageChartResponse;
import fr.tiogars.data.vehicles.carmileage.services.CarMileageChartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "car-mileage", description = "Operations liees aux releves de kilometrage des voitures.")
public class CarMileageChartController {

    private final CarMileageChartService carMileageChartService;

    public CarMileageChartController(CarMileageChartService carMileageChartService) {
        this.carMileageChartService = carMileageChartService;
    }

    @GetMapping("/car-mileage/chart")
    @Operation(summary = "Graphique des releves", description = "Cette operation retourne les points de kilometrage pour alimenter le graphique d'une voiture.")
    public ResponseEntity<CarMileageChartResponse> chartCarMileages(
        @Parameter(description = "Identifiant de la voiture.", example = "123e4567-e89b-12d3-a456-426614174000")
        @RequestParam String carId
    ) {
        return ResponseEntity.ok(carMileageChartService.chartCarMileages(carId));
    }
}
