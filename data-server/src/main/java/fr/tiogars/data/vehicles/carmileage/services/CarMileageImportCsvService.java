package fr.tiogars.data.vehicles.carmileage.services;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import fr.tiogars.data.common.csv.CsvSupport;
import fr.tiogars.data.vehicles.carmileage.forms.CarMileageImportForm;
import fr.tiogars.data.vehicles.carmileage.models.CarMileage;
import fr.tiogars.data.vehicles.carmileage.models.CarMileageImportResult;

@Service
public class CarMileageImportCsvService {

    private final CarMileageImportService carMileageImportService;

    public CarMileageImportCsvService(CarMileageImportService carMileageImportService) {
        this.carMileageImportService = carMileageImportService;
    }

    public CarMileageImportResult importCarMileagesFromCsv(String csvContent) {
        if (csvContent == null || csvContent.isBlank()) {
            return carMileageImportService.importCarMileages(new CarMileageImportForm());
        }

        char delimiter = CsvSupport.detectDelimiter(csvContent);
        List<List<String>> rows = CsvSupport.parseCsvRows(csvContent, delimiter);
        if (rows.isEmpty()) {
            return carMileageImportService.importCarMileages(new CarMileageImportForm());
        }

        CsvColumnMapping mapping = resolveColumnMapping(rows.getFirst());
        int startIndex = mapping.hasHeader() ? 1 : 0;
        List<CarMileage> items = new ArrayList<>();

        for (int i = startIndex; i < rows.size(); i++) {
            List<String> row = rows.get(i);
            if (row.isEmpty()) {
                continue;
            }

            CarMileage carMileage = new CarMileage();
            carMileage.setCarId(CsvSupport.valueAt(row, mapping.carIdIndex()));
            carMileage.setReadingAt(parseDateTime(CsvSupport.valueAt(row, mapping.readingAtIndex())));
            carMileage.setOdometerKm(parseInteger(CsvSupport.valueAt(row, mapping.odometerKmIndex())));
            carMileage.setFuelVolumeLiters(parseBigDecimal(CsvSupport.valueAt(row, mapping.fuelVolumeLitersIndex())));
            carMileage.setFullTank(parseBoolean(CsvSupport.valueAt(row, mapping.fullTankIndex())));

            if (isEmpty(carMileage)) {
                continue;
            }

            items.add(carMileage);
        }

        CarMileageImportForm form = new CarMileageImportForm();
        form.setItems(items);
        return carMileageImportService.importCarMileages(form);
    }

    private static boolean isEmpty(CarMileage item) {
        return (item.getCarId() == null || item.getCarId().isBlank())
            && item.getReadingAt() == null
            && item.getOdometerKm() == null
            && item.getFuelVolumeLiters() == null
            && item.getFullTank() == null;
    }

    private static LocalDateTime parseDateTime(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        try {
            return LocalDateTime.parse(value.trim());
        } catch (RuntimeException ex) {
            return null;
        }
    }

    private static Integer parseInteger(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        try {
            return Integer.parseInt(value.trim());
        } catch (RuntimeException ex) {
            return null;
        }
    }

    private static BigDecimal parseBigDecimal(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        try {
            return new BigDecimal(value.trim());
        } catch (RuntimeException ex) {
            return null;
        }
    }

    private static Boolean parseBoolean(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        String normalized = value.trim().toLowerCase(java.util.Locale.ROOT);
        if ("true".equals(normalized) || "1".equals(normalized) || "oui".equals(normalized) || "yes".equals(normalized)) {
            return Boolean.TRUE;
        }
        if ("false".equals(normalized) || "0".equals(normalized) || "non".equals(normalized) || "no".equals(normalized)) {
            return Boolean.FALSE;
        }
        return null;
    }

    private record CsvColumnMapping(
        boolean hasHeader,
        int carIdIndex,
        int readingAtIndex,
        int odometerKmIndex,
        int fuelVolumeLitersIndex,
        int fullTankIndex
    ) { }

    private static CsvColumnMapping resolveColumnMapping(List<String> firstRow) {
        if (firstRow == null || firstRow.isEmpty()) {
            return new CsvColumnMapping(false, 1, 2, 3, 4, 5);
        }

        int carIdIndex = -1;
        int readingAtIndex = -1;
        int odometerKmIndex = -1;
        int fuelVolumeLitersIndex = -1;
        int fullTankIndex = -1;

        for (int i = 0; i < firstRow.size(); i++) {
            String normalizedHeader = CsvSupport.normalizeHeader(firstRow.get(i), true);
            if ("carid".equals(normalizedHeader)) {
                carIdIndex = i;
            }
            if ("readingat".equals(normalizedHeader)) {
                readingAtIndex = i;
            }
            if ("odometerkm".equals(normalizedHeader)) {
                odometerKmIndex = i;
            }
            if ("fuelvolumeliters".equals(normalizedHeader)) {
                fuelVolumeLitersIndex = i;
            }
            if ("fulltank".equals(normalizedHeader)) {
                fullTankIndex = i;
            }
        }

        if (carIdIndex >= 0 || readingAtIndex >= 0 || odometerKmIndex >= 0 || fuelVolumeLitersIndex >= 0 || fullTankIndex >= 0) {
            return new CsvColumnMapping(
                true,
                carIdIndex >= 0 ? carIdIndex : 1,
                readingAtIndex >= 0 ? readingAtIndex : 2,
                odometerKmIndex >= 0 ? odometerKmIndex : 3,
                fuelVolumeLitersIndex >= 0 ? fuelVolumeLitersIndex : 4,
                fullTankIndex >= 0 ? fullTankIndex : 5
            );
        }

        return new CsvColumnMapping(false, 1, 2, 3, 4, 5);
    }
}
