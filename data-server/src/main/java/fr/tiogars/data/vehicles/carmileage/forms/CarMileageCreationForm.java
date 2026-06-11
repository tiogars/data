package fr.tiogars.data.vehicles.carmileage.forms;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;

public class CarMileageCreationForm {

    @Schema(description = "L'identifiant de la voiture.", example = "123e4567-e89b-12d3-a456-426614174000")
    private String carId;

    @Schema(description = "La date et l'heure du releve. Si non renseignee, la date/heure actuelle est appliquee.", example = "2026-06-11T08:30:00")
    private LocalDateTime readingAt;

    @Schema(description = "Le kilometrage releve en kilometres.", example = "120500")
    private Integer odometerKm;

    @Schema(description = "Le volume de carburant ajoute en litres.", example = "42.50")
    private BigDecimal fuelVolumeLiters;

    @Schema(description = "Indique si le plein complet a ete fait.", example = "true")
    private Boolean fullTank;

    public String getCarId() {
        return carId;
    }

    public void setCarId(String carId) {
        this.carId = carId;
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

    public BigDecimal getFuelVolumeLiters() {
        return fuelVolumeLiters;
    }

    public void setFuelVolumeLiters(BigDecimal fuelVolumeLiters) {
        this.fuelVolumeLiters = fuelVolumeLiters;
    }

    public Boolean getFullTank() {
        return fullTank;
    }

    public void setFullTank(Boolean fullTank) {
        this.fullTank = fullTank;
    }
}
