package fr.tiogars.data.cave.vintag.forms;

import io.swagger.v3.oas.annotations.media.Schema;

public class VinTagCreationForm {
    @Schema(description = "Le nom du tag de vin.", example = "Millesime")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
