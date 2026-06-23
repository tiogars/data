package fr.tiogars.data.cave.circonstance.forms;

import io.swagger.v3.oas.annotations.media.Schema;

public class CirconstanceCreationForm {
    @Schema(description = "Le nom de la circonstance.", example = "Anniversaire")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
