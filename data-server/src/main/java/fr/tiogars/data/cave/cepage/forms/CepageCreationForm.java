package fr.tiogars.data.cave.cepage.forms;

import io.swagger.v3.oas.annotations.media.Schema;

public class CepageCreationForm {
    @Schema(description = "Le nom du cépage.", example = "Chardonnay")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
