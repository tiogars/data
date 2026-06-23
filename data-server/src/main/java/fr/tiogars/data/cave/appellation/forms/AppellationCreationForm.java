package fr.tiogars.data.cave.appellation.forms;

import io.swagger.v3.oas.annotations.media.Schema;

public class AppellationCreationForm {
    @Schema(description = "Le nom de l'appellation.", example = "Champagne")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
