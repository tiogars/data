package fr.tiogars.data.vehicles.carmileage.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.vehicles.carmileage.models.CarMileageImportResult;
import fr.tiogars.data.vehicles.carmileage.services.CarMileageImportCsvService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "car-mileage", description = "Operations liees aux releves de kilometrage des voitures.")
public class CarMileageImportCsvController {

    private final CarMileageImportCsvService carMileageImportCsvService;

    public CarMileageImportCsvController(CarMileageImportCsvService carMileageImportCsvService) {
        this.carMileageImportCsvService = carMileageImportCsvService;
    }

    @PostMapping(value = "/car-mileage/import/csv", consumes = { "text/csv", "text/plain" })
    @Operation(summary = "Importer les releves de kilometrage en CSV", description = "Importe des releves au format CSV et applique les regles d'import existantes.")
    public ResponseEntity<CarMileageImportResult> importCarMileagesCsv(@RequestBody(required = false) String csvContent) {
        return ResponseEntity.ok(carMileageImportCsvService.importCarMileagesFromCsv(csvContent));
    }
}
