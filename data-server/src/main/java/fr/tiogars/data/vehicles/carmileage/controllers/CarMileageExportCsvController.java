package fr.tiogars.data.vehicles.carmileage.controllers;

import java.nio.charset.StandardCharsets;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.vehicles.carmileage.services.CarMileageExportCsvService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "car-mileage", description = "Operations liees aux releves de kilometrage des voitures.")
public class CarMileageExportCsvController {

    private final CarMileageExportCsvService carMileageExportCsvService;

    public CarMileageExportCsvController(CarMileageExportCsvService carMileageExportCsvService) {
        this.carMileageExportCsvService = carMileageExportCsvService;
    }

    @GetMapping(value = "/car-mileage/export/csv", produces = "text/csv")
    @Operation(summary = "Exporter les releves de kilometrage en CSV", description = "Retourne la liste complete des releves au format CSV.")
    public ResponseEntity<String> exportCarMileagesCsv() {
        MediaType csvContentType = new MediaType("text", "csv", StandardCharsets.UTF_8);
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"car-mileage-export.csv\"")
            .contentType(csvContentType)
            .body(carMileageExportCsvService.exportCarMileagesAsCsv());
    }
}
