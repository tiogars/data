package fr.tiogars.data.vehicles.car.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.vehicles.car.models.CarImportResult;
import fr.tiogars.data.vehicles.car.services.CarImportCsvService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "car", description = "Operations liees a la gestion des voitures.")
public class CarImportCsvController {

    private final CarImportCsvService carImportCsvService;

    public CarImportCsvController(CarImportCsvService carImportCsvService) {
        this.carImportCsvService = carImportCsvService;
    }

    @PostMapping(value = "/car/import/csv", consumes = { "text/csv", "text/plain" })
    @Operation(summary = "Importer les voitures en CSV", description = "Importe des voitures au format CSV et applique les regles d'import existantes.")
    public ResponseEntity<CarImportResult> importCarsCsv(@RequestBody(required = false) String csvContent) {
        return ResponseEntity.ok(carImportCsvService.importCarsFromCsv(csvContent));
    }
}
