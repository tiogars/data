package fr.tiogars.data.vehicles.car.services;

import org.springframework.stereotype.Service;

import fr.tiogars.data.common.csv.CsvSupport;
import fr.tiogars.data.vehicles.car.models.Car;

@Service
public class CarExportCsvService {

    private final CarExportService carExportService;

    public CarExportCsvService(CarExportService carExportService) {
        this.carExportService = carExportService;
    }

    public String exportCarsAsCsv() {
        StringBuilder csv = new StringBuilder();
        csv.append("name,description\n");

        for (Car item : carExportService.exportCars().getItems()) {
            csv.append(CsvSupport.escapeCsv(item != null ? item.getName() : null));
            csv.append(',');
            csv.append(CsvSupport.escapeCsv(item != null ? item.getDescription() : null));
            csv.append('\n');
        }

        return csv.toString();
    }
}
