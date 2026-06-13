package fr.tiogars.data.vehicles.carmileage.models;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;

import fr.tiogars.data.sync.services.SyncUpdatedItem;
import io.swagger.v3.oas.annotations.media.Schema;

public class CarMileage implements SyncUpdatedItem {

    @Schema(description = "L'identifiant unique du releve.", example = "123e4567-e89b-12d3-a456-426614174000")
    private String id;

    @Schema(description = "L'identifiant de la voiture.", example = "123e4567-e89b-12d3-a456-426614174000")
    private String carId;

    @Schema(description = "Le nom de la voiture.", example = "Clio 3")
    private String carName;

    @Schema(description = "La date et l'heure du releve.", example = "2026-06-11T08:30:00")
    private LocalDateTime readingAt;

    @Schema(description = "Le kilometrage releve en kilometres.", example = "120500")
    private Integer odometerKm;

    @Schema(description = "Le volume de carburant ajoute en litres.", example = "42.50")
    private BigDecimal fuelVolumeLiters;

    @Schema(description = "Indique si le plein complet a ete fait.", example = "true")
    private Boolean fullTank;

    @Schema(description = "Date de derniere mise a jour de l'element.", example = "2026-06-13T11:45:00Z")
    private Instant updatedAt;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCarId() {
        return carId;
    }

    public void setCarId(String carId) {
        this.carId = carId;
    }

    public String getCarName() {
        return carName;
    }

    public void setCarName(String carName) {
        this.carName = carName;
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

    @Override
    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }
}
