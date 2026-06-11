package fr.tiogars.data.vehicles.car.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.vehicles.car.forms.CarImportForm;
import fr.tiogars.data.vehicles.car.models.CarImportResult;
import fr.tiogars.data.vehicles.car.services.CarImportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "car", description = "Operations liees a la gestion des voitures.")
public class CarImportController {

    private final CarImportService carImportService;

    public CarImportController(CarImportService carImportService) {
        this.carImportService = carImportService;
    }

    @PostMapping("/car/import")
    @Operation(summary = "Importer les voitures", description = "Importe des voitures depuis un format JSON.")
    public ResponseEntity<CarImportResult> importCars(@RequestBody CarImportForm form) {
        return ResponseEntity.ok(carImportService.importCars(form));
    }
}
