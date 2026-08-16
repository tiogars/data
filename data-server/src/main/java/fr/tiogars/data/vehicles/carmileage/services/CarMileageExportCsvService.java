package fr.tiogars.data.vehicles.carmileage.services;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import fr.tiogars.data.common.csv.CsvSupport;
import fr.tiogars.data.vehicles.carmileage.models.CarMileage;

@Service
public class CarMileageExportCsvService {

    private final CarMileageExportService carMileageExportService;

    public CarMileageExportCsvService(CarMileageExportService carMileageExportService) {
        this.carMileageExportService = carMileageExportService;
    }

    public String exportCarMileagesAsCsv() {
        StringBuilder csv = new StringBuilder();
        csv.append("carName,vehicleRegistrationPlate,readingAt,odometerKm,fuelVolumeLiters,fullTank\n");

        for (CarMileage item : carMileageExportService.exportCarMileages().getItems()) {
            csv.append(CsvSupport.escapeCsv(item != null ? item.getCarName() : null));
            csv.append(',');
            csv.append(CsvSupport.escapeCsv(item != null ? item.getVehicleRegistrationPlate() : null));
            csv.append(',');
            csv.append(CsvSupport.escapeCsv(item != null && item.getReadingAt() != null ? item.getReadingAt().toString() : null));
            csv.append(',');
            csv.append(CsvSupport.escapeCsv(item != null && item.getOdometerKm() != null ? item.getOdometerKm().toString() : null));
            csv.append(',');
            csv.append(CsvSupport.escapeCsv(item != null ? formatFuelVolume(item.getFuelVolumeLiters()) : null));
            csv.append(',');
            csv.append(CsvSupport.escapeCsv(item != null && item.getFullTank() != null ? item.getFullTank().toString() : null));
            csv.append('\n');
        }

        return csv.toString();
    }

    private static String formatFuelVolume(BigDecimal fuelVolumeLiters) {
        if (fuelVolumeLiters == null) {
            return null;
        }
        return fuelVolumeLiters.stripTrailingZeros().toPlainString();
    }
}
