package fr.tiogars.data.vehicles.carmileage.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;

import fr.tiogars.data.vehicles.carmileage.models.CarMileage;
import fr.tiogars.data.vehicles.carmileage.models.CarMileageListResponse;

class CarMileageExportCsvServiceTest {

    @Test
    void shouldStripTrailingZerosWhenExportingFuelVolume() {
        CarMileage mileage = new CarMileage();
        mileage.setCarName("Clio test");
        mileage.setVehicleRegistrationPlate("AA-123-AA");
        mileage.setReadingAt(LocalDateTime.of(2026, 6, 1, 8, 0));
        mileage.setOdometerKm(100000);
        mileage.setFuelVolumeLiters(new BigDecimal("35.50"));
        mileage.setFullTank(true);

        CarMileageExportService exportService = mock(CarMileageExportService.class);
        when(exportService.exportCarMileages()).thenReturn(new CarMileageListResponse(List.of(mileage), 1));

        String csv = new CarMileageExportCsvService(exportService).exportCarMileagesAsCsv();

        assertThat(csv).contains("carName,vehicleRegistrationPlate,readingAt,odometerKm,fuelVolumeLiters,fullTank");
        assertThat(csv).contains("Clio test,AA-123-AA,2026-06-01T08:00,100000,35.5,true");
        assertThat(csv).doesNotContain("35.50");
    }
}
