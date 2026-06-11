package fr.tiogars.data.vehicles.carmileage.models;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;

public class CarMileageChartPoint {

    @Schema(description = "La date et l'heure du releve.", example = "2026-06-11T08:30:00")
    private LocalDateTime readingAt;

    @Schema(description = "Le kilometrage releve en kilometres.", example = "120500")
    private Integer odometerKm;

    public CarMileageChartPoint(LocalDateTime readingAt, Integer odometerKm) {
        this.readingAt = readingAt;
        this.odometerKm = odometerKm;
    }

    public LocalDateTime getReadingAt() {
        return readingAt;
    }

    public void setReadingAt(LocalDateTime readingAt) {
        this.readingAt = readingAt;
    }

    public Integer getOdometerKm() {
        return odometerKm;
    }

    public void setOdometerKm(Integer odometerKm) {
        this.odometerKm = odometerKm;
    }
}
