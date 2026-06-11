package fr.tiogars.data.vehicles.car.controllers;

import java.nio.charset.StandardCharsets;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.vehicles.car.services.CarExportCsvService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "car", description = "Operations liees a la gestion des voitures.")
public class CarExportCsvController {

    private final CarExportCsvService carExportCsvService;

    public CarExportCsvController(CarExportCsvService carExportCsvService) {
        this.carExportCsvService = carExportCsvService;
    }

    @GetMapping(value = "/car/export/csv", produces = "text/csv")
    @Operation(summary = "Exporter les voitures en CSV", description = "Retourne la liste complete des voitures au format CSV.")
    public ResponseEntity<String> exportCarsCsv() {
        MediaType csvContentType = new MediaType("text", "csv", StandardCharsets.UTF_8);
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"car-export.csv\"")
            .contentType(csvContentType)
            .body(carExportCsvService.exportCarsAsCsv());
    }
}
