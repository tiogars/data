package fr.tiogars.data.vehicles.car.forms;

import io.swagger.v3.oas.annotations.media.Schema;

public class CarCreationForm {

    @Schema(description = "Le nom de la voiture.", example = "Clio 3")
    private String name;

    @Schema(description = "La description optionnelle de la voiture.", example = "Vehicule principal")
    private String description;

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
}
