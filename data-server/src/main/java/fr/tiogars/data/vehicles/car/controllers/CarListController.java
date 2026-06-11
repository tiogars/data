package fr.tiogars.data.vehicles.car.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.vehicles.car.models.CarListResponse;
import fr.tiogars.data.vehicles.car.services.CarListService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "car", description = "Operations liees a la gestion des voitures.")
public class CarListController {

    private final CarListService carListService;

    public CarListController(CarListService carListService) {
        this.carListService = carListService;
    }

    @GetMapping("/car/list")
    @Operation(summary = "Lister les voitures", description = "Cette operation permet de recuperer la liste des voitures.")
    public ResponseEntity<CarListResponse> listCars() {
        return ResponseEntity.ok(carListService.listCars());
    }
}
