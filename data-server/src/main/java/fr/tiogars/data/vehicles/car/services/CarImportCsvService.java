package fr.tiogars.data.vehicles.car.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import fr.tiogars.data.common.csv.CsvSupport;
import fr.tiogars.data.vehicles.car.forms.CarImportForm;
import fr.tiogars.data.vehicles.car.models.Car;
import fr.tiogars.data.vehicles.car.models.CarImportResult;

@Service
public class CarImportCsvService {

    private final CarImportService carImportService;

    public CarImportCsvService(CarImportService carImportService) {
        this.carImportService = carImportService;
    }

    public CarImportResult importCarsFromCsv(String csvContent) {
        if (csvContent == null || csvContent.isBlank()) {
            return carImportService.importCars(new CarImportForm());
        }

        char delimiter = CsvSupport.detectDelimiter(csvContent);
        List<List<String>> rows = CsvSupport.parseCsvRows(csvContent, delimiter);
        if (rows.isEmpty()) {
            return carImportService.importCars(new CarImportForm());
        }

        CsvColumnMapping mapping = resolveColumnMapping(rows.getFirst());
        int startIndex = mapping.hasHeader() ? 1 : 0;
        List<Car> items = new ArrayList<>();

        for (int i = startIndex; i < rows.size(); i++) {
            List<String> row = rows.get(i);
            if (row.isEmpty()) {
                continue;
            }

            String name = CsvSupport.valueAt(row, mapping.nameIndex());
            String description = CsvSupport.valueAt(row, mapping.descriptionIndex());
            if ((name == null || name.isBlank()) && (description == null || description.isBlank())) {
                continue;
            }

            Car car = new Car();
            car.setName(name);
            car.setDescription(description);
            items.add(car);
        }

        CarImportForm form = new CarImportForm();
        form.setItems(items);
        return carImportService.importCars(form);
    }

    private record CsvColumnMapping(boolean hasHeader, int nameIndex, int descriptionIndex) { }

    private static CsvColumnMapping resolveColumnMapping(List<String> firstRow) {
        if (firstRow == null || firstRow.isEmpty()) {
            return new CsvColumnMapping(false, 0, 1);
        }

        int nameIndex = -1;
        int descriptionIndex = -1;

        for (int i = 0; i < firstRow.size(); i++) {
            String normalizedHeader = CsvSupport.normalizeHeader(firstRow.get(i));
            if ("name".equals(normalizedHeader)) {
                nameIndex = i;
            }
            if ("description".equals(normalizedHeader)) {
                descriptionIndex = i;
            }
        }

        if (nameIndex >= 0 || descriptionIndex >= 0) {
            return new CsvColumnMapping(true, nameIndex >= 0 ? nameIndex : 0, descriptionIndex >= 0 ? descriptionIndex : 1);
        }

        return new CsvColumnMapping(false, 0, 1);
    }
}
