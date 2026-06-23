package fr.tiogars.data.cave.couleur.forms;

import io.swagger.v3.oas.annotations.media.Schema;

public class CouleurCreationForm {
    @Schema(description = "Le nom de la couleur.", example = "Rouge")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
