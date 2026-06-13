package fr.tiogars.data.vehicles.car.models;

import java.time.Instant;

import io.swagger.v3.oas.annotations.media.Schema;
import fr.tiogars.data.sync.services.SyncUpdatedItem;

public class Car implements SyncUpdatedItem {

    @Schema(description = "L'identifiant unique de la voiture.", example = "123e4567-e89b-12d3-a456-426614174000")
    private String id;

    @Schema(description = "Le nom de la voiture.", example = "Clio 3")
    private String name;

    @Schema(description = "Le numéro d'immatriculation de la voiture.", example = "AB-123-CD")
    private String vehicleRegistrationPlate;

    @Schema(description = "La description optionnelle de la voiture.", example = "Vehicule principal")
    private String description;

    @Schema(description = "Date de derniere mise a jour de l'element.", example = "2026-06-13T11:45:00Z")
    private Instant updatedAt;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVehicleRegistrationPlate() {
        return vehicleRegistrationPlate;
    }

    public void setVehicleRegistrationPlate(String vehicleRegistrationPlate) {
        this.vehicleRegistrationPlate = vehicleRegistrationPlate;
    }

    @Override
    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }
}
